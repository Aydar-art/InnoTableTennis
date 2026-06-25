import { db } from '$lib/server/db/index';

export function init() {
  db.exec(`
    CREATE TABLE IF NOT EXISTS tournaments (
      id INTEGER PRIMARY KEY,
      title TEXT NOT NULL,
      slug TEXT NOT NULL,
      date TEXT NOT NULL,
      participantsCount INTEGER NOT NULL,
      gamesPlayedCount INTEGER NOT NULL,
      customText TEXT NOT NULL
    )
  `);

  db.exec(`
    CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY,
      email TEXT NOT NULL,
      password TEXT NOT NULL
    )
  `);
}
