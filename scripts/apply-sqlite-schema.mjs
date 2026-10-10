import { readFileSync } from 'node:fs';
import { DatabaseSync } from 'node:sqlite';

const dbPath = 'src/main/resources/db/sysinvent.db';
const schemaPath = 'src/main/resources/db/schema-sqlite.sql';

const db = new DatabaseSync(dbPath);
try {
  db.exec(readFileSync(schemaPath, 'utf8'));

  const tables = db
    .prepare("SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%' ORDER BY name")
    .all()
    .map((row) => row.name);

  console.log(`Base de datos: ${dbPath}`);
  console.log(`Tablas (${tables.length}):`);
  for (const table of tables) {
    console.log(`- ${table}`);
  }
} finally {
  db.close();
}
