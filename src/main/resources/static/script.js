/* Frontend behavior updated to use the backend API */
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('parking-form');
  const nameInput = document.getElementById('name');
  const vehicleInput = document.getElementById('vehicle');
  const locationSelect = document.getElementById('location');
  const durationInput = document.getElementById('duration');
  const tableBody = document.querySelector('#reservations tbody');
  const announce = document.getElementById('announce');
  const clearBtn = document.getElementById('clear-btn');

  let reservations = [];
  loadReservations();

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = nameInput.value.trim();
    const vehicle = vehicleInput.value.trim();
    const location = locationSelect.value;
    const duration = Number(durationInput.value) || 1;

    if (!name || !vehicle || !location) {
      announceMessage('Please complete all required fields.');
      return;
    }

    const item = { name, vehicle, location, duration };
    try {
      const saved = await saveReservationToServer(item);
      reservations.push(saved);
      appendRow(saved);
      form.reset();
      durationInput.value = 1;
      nameInput.focus();
      announceMessage(`${saved.name} — reservation added for ${saved.location} for ${saved.duration} hour${saved.duration>1?'s':''}.`);
    } catch (err) {
      console.error(err);
      announceMessage('Could not save reservation.');
    }
  });

  clearBtn.addEventListener('click', async () => {
    if (!reservations.length) {
      announceMessage('There are no reservations to clear.');
      return;
    }
    if (!confirm('Clear all reservations? This cannot be undone.')) return;
    try {
      // delete individually
      for (const r of [...reservations]) {
        await deleteReservationOnServer(r.id);
      }
      reservations = [];
      renderReservations();
      announceMessage('All reservations cleared.');
    } catch (err) {
      console.error(err);
      announceMessage('Error clearing reservations.');
    }
  });

  function appendRow(item) {
    const tr = buildRow(item);
    tableBody.appendChild(tr);
    scrollRowIntoView(tr);
  }

  function buildRow(item) {
    const tr = document.createElement('tr');
    tr.setAttribute('data-id', item.id);

    const tdId = document.createElement('td'); tdId.textContent = item.id;
    const tdName = document.createElement('td'); tdName.textContent = item.name;
    const tdVehicle = document.createElement('td'); tdVehicle.textContent = item.vehicle;
    const tdLocation = document.createElement('td'); tdLocation.textContent = item.location;
    const tdDuration = document.createElement('td'); tdDuration.textContent = item.duration;
    const tdActions = document.createElement('td');

    const delBtn = document.createElement('button');
    delBtn.className = 'row-action';
    delBtn.type = 'button';
    delBtn.textContent = 'Delete';
    delBtn.setAttribute('aria-label', `Delete reservation for ${item.name} at ${item.location}`);
    delBtn.addEventListener('click', async () => {
      await handleDelete(item.id, tr);
    });
    delBtn.addEventListener('keydown', (ev) => {
      if (ev.key === 'Enter' || ev.key === ' ') {
        ev.preventDefault();
        delBtn.click();
      }
    });

    tdActions.appendChild(delBtn);

    tr.appendChild(tdId);
    tr.appendChild(tdName);
    tr.appendChild(tdVehicle);
    tr.appendChild(tdLocation);
    tr.appendChild(tdDuration);
    tr.appendChild(tdActions);
    return tr;
  }

  async function handleDelete(id, rowElement) {
    try {
      const ok = await deleteReservationOnServer(id);
      if (ok) {
        reservations = reservations.filter(r => r.id !== id);
        rowElement.remove();
        announceMessage('Reservation removed.');
        if (!reservations.length) renderReservations();
      } else {
        announceMessage('Could not delete reservation.');
      }
    } catch (err) {
      console.error(err);
      announceMessage('Could not delete reservation.');
    }
  }

  function renderReservations() {
    tableBody.innerHTML = '';
    if (!reservations.length) {
      const tr = document.createElement('tr');
      const td = document.createElement('td');
      td.colSpan = 6;
      td.textContent = 'No reservations yet.';
      td.style.color = '#6b7280';
      tr.appendChild(td);
      tableBody.appendChild(tr);
      announceMessage('No reservations yet.');
      return;
    }
    reservations.forEach(item => tableBody.appendChild(buildRow(item)));
  }

  async function saveReservationToServer(item) {
    const res = await fetch('/api/reservations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(item)
    });
    if (!res.ok) throw new Error('Save failed');
    return await res.json();
  }

  async function deleteReservationOnServer(id) {
    const res = await fetch(`/api/reservations/${id}`, { method: 'DELETE' });
    return res.ok;
  }

  async function loadReservations() {
    try {
      const res = await fetch('/api/reservations');
      if (!res.ok) {
        announceMessage('Could not load reservations from server.');
        renderReservations();
        return;
      }
      reservations = await res.json();
      renderReservations();
    } catch (err) {
      console.error(err);
      announceMessage('Could not load reservations.');
      renderReservations();
    }
  }

  function announceMessage(msg) {
    if (!announce) return;
    announce.textContent = msg;
  }

  function scrollRowIntoView(row) {
    row.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }
});
