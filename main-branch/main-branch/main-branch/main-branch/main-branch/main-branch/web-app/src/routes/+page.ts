export async function load({ fetch }) {
  try {
    const response = await fetch('./content/index.json');
    if (!response.ok) {
      return { entries: [] };
    }
    const entries = await response.json();
    return { entries };
  } catch (e) {
    return { entries: [] };
  }
}

