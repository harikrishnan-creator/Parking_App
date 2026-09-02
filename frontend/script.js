/* Accessible frontend behavior for Parking App
   - Stores reservations in localStorage
   - Updates table and aria-live announcements
   - Keyboard accessible actions (delete via Enter/Space)
*/
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('parking-form');
  const nameInput = document.getElementById('name');
  const vehicleInput = document.getElementById('vehicle');
  const locationSelect = document.getElementById('location');
  const durationInput = document.getElementById('duration');
  const tableBody = document.querySelector('#reservations tbody');
  const announce = document.getElementById('announce');
  const clearBtn = document.getElementById('clear-btn');
  const STORAGE_KEY = 'parking_app_reservations_v1';

  let reservations = loadReservations();
  renderReservations();

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    const name = nameInput.value.trim();
    const vehicle = vehicleInput.value.trim();
    const location = locationSelect.value;
    const duration = Number(durationInput.value) || 1;

    if (!name || !vehicle || !location) {
      announceMessage('Please complete all required fields.');
      return;
    }

    const id = cryptoRandomId();
    const item = { id, name, vehicle, location, duration, createdAt: new Date().toISOString() };
    reservations.push(item);
    saveReservations();
    appendRow(item);
    form.reset();
    durationInput.value = 1;
    nameInput.focus();
    announceMessage(`${name} — reservation added for ${location} for ${duration} hour${duration>1?'s':''}.`);
  });

  clearBtn.addEventListener('click', () => {
    if (!reservations.length) {
      announceMessage('There are no reservations to clear.');
      return;
    }
    if (!confirm('Clear all reservations? This cannot be undone.')) return;
    reservations = [];
    saveReservations();
    renderReservations();
    announceMessage('All reservations cleared.');
  });

  function appendRow(item) {
    const tr = buildRow(item);
    tableBody.appendChild(tr);
    scrollRowIntoView(tr);
  }

  function buildRow(item) {
    const tr = document.createElement('tr');
    tr.setAttribute('data-id', item.id);

    const tdName = document.createElement('td');
    tdName.textContent = item.name;
    const tdVehicle = document.createElement('td');
    tdVehicle.textContent = item.vehicle;
    const tdLocation = document.createElement('td');
    tdLocation.textContent = item.location;
    const tdDuration = document.createElement('td');
    tdDuration.textContent = item.duration;
    const tdActions = document.createElement('td');

    const delBtn = document.createElement('button');
    delBtn.className = 'row-action';
    delBtn.type = 'button';
    delBtn.textContent = 'Delete';
    delBtn.setAttribute('aria-label', `Delete reservation for ${item.name} at ${item.location}`);
    delBtn.addEventListener('click', () => deleteReservation(item.id, tr));
    // keyboard support for Enter/Space
    delBtn.addEventListener('keydown', (ev) => {
      if (ev.key === 'Enter' || ev.key === ' ') {
        ev.preventDefault();
        delBtn.click();
      }
    });

    tdActions.appendChild(delBtn);

    tr.appendChild(tdName);
    tr.appendChild(tdVehicle);
    tr.appendChild(tdLocation);
    tr.appendChild(tdDuration);
    tr.appendChild(tdActions);
    return tr;
  }

  function deleteReservation(id, rowElement) {
    const idx = reservations.findIndex(r => r.id === id);
    if (idx === -1) return;
    const [removed] = reservations.splice(idx, 1);
    saveReservations();
    // focus management: move focus to next row's delete button or to clear button
    const nextFocus = (rowElement.nextElementSibling && rowElement.nextElementSibling.querySelector('.row-action'))
      || document.getElementById('clear-btn');
    rowElement.remove();
    announceMessage(`Removed reservation for ${removed.name} at ${removed.location}.`);
    if (nextFocus) nextFocus.focus();
    if (!reservations.length) renderReservations();
  }

  function renderReservations() {
    tableBody.innerHTML = '';
    if (!reservations.length) {
      const tr = document.createElement('tr');
      const td = document.createElement('td');
      td.colSpan = 5;
      td.textContent = 'No reservations yet.';
      td.style.color = '#6b7280';
      tr.appendChild(td);
      tableBody.appendChild(tr);
      announceMessage('No reservations yet.');
      return;
    }
    reservations.forEach(item => tableBody.appendChild(buildRow(item)));
  }

  function saveReservations() {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(reservations));
    } catch (err) {
      console.warn('Could not save reservations to localStorage', err);
    }
  }

  function loadReservations() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) return [];
      return JSON.parse(raw);
    } catch (err) {
      console.warn('Error parsing reservations from storage', err);
      return [];
    }
  }

  function announceMessage(msg) {
    if (!announce) return;
    announce.textContent = msg;
  }

  function scrollRowIntoView(row) {
    row.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }

  function cryptoRandomId() {
    if (window.crypto && crypto.randomUUID) return crypto.randomUUID();
    return 'id-' + Math.random().toString(36).slice(2,9);
  }
});
