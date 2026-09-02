# Parking App — Frontend

This folder contains a simple, accessible frontend for the Parking App.

Files
- index.html — accessible UI (form, reservations table, skip link, aria-live status)
- styles.css — responsive, high-contrast styles and visible focus states
- script.js — client-side logic (localStorage, keyboard support, announcements)

How to run
1. Clone the repository.
2. Serve the frontend folder with a static server (recommended) or open `frontend/index.html` in your browser.
   - Example (Python): `python -m http.server --directory frontend 8000`
3. Open http://localhost:8000 in your browser.

Integration notes
- Currently the frontend stores reservations in localStorage. If you'd like backend persistence, tell me the API endpoints (GET/POST/DELETE) and I will update `script.js` to call them and add brief CORS guidance.

Accessibility
- Skip link, labels, aria-live region, keyboard support on actions, and clear focus styles were included. Test with keyboard-only navigation and a screen reader.

Next steps
- I can commit these files for you now, or adjust color, fields, or integrate the Java backend if you provide the API contract.
