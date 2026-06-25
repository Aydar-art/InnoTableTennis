import Database from 'better-sqlite3';
import type { ITournament, IUser } from '$lib/types';
import { init } from '$lib/server/db/init';
import { slugify } from '$lib/utils';

export const db = new Database('db.sqlite', { verbose: console.log });
init();

export function getTournaments(status: 'active' | 'archived'): ITournament[] {
  let stmt;
  if (status == 'active') {
    stmt = db.prepare("SELECT * FROM tournaments WHERE customText = 'In progress'");
  } else if (status == 'archived') {
    stmt = db.prepare("SELECT * FROM tournaments WHERE customText = 'Archived'");
  } else {
    stmt = db.prepare('SELECT * FROM tournaments');
  }
  const tournaments = stmt.all();
  return tournaments as ITournament[];
}

export function addUser(email: string, password: string) {
  const stmt = db.prepare('INSERT INTO users (email, password) VALUES (?, ?)');
  stmt.run(email, password);
}

export function getUser(email: string): IUser | undefined {
  const stmt = db.prepare('SELECT * FROM users WHERE email = ?');
  return stmt.get(email) as IUser;
}

export function archiveTournament(slug: string) {
  const stmt = db.prepare("UPDATE tournaments SET customText = 'Archived' WHERE slug = ?");
  stmt.run(slug);
}

export function createTournament(tournament: ITournament) {
  const stmt = db.prepare('INSERT INTO tournaments (title, slug, date, participantsCount, gamesPlayedCount, customText) VALUES (?, ?, ?, ?, ?, ?)');
  stmt.run(tournament.title, tournament.slug, tournament.date, tournament.participantsCount, tournament.gamesPlayedCount, tournament.customText);
}

export function getTournament(slug: string): ITournament | undefined {
  const stmt = db.prepare('SELECT * FROM tournaments WHERE slug = ?');
  return stmt.get(slug) as ITournament;
}

export function deleteTournament(slug: string) {
  const stmt = db.prepare('DELETE FROM tournaments WHERE slug = ?');
  stmt.run(slug);
}

export function updateTournament(slug: string, title: string) {
  const stmt = db.prepare('UPDATE tournaments SET title = ?, slug = ? WHERE slug = ?');
  stmt.run(title, slugify(title), slug);
}
