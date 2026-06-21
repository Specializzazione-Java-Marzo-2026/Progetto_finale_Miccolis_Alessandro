#!/usr/bin/env bash
HOST=${1:-localhost}
PORT=${2:-3306}
DB=${3:-Progetto_finale}
USER=${4:-root}
PASS=${5:-root}
SQLDIR="$(dirname "$0")/../sql"

echo "Running SQL scripts against $HOST:$PORT/$DB"
for f in drop.sql create.sql insert.sql; do
  FILE="$SQLDIR/$f"
  if [ -f "$FILE" ]; then
    echo "Executing $FILE..."
    mysql -h "$HOST" -P "$PORT" -u "$USER" -p"$PASS" "$DB" < "$FILE"
    if [ $? -ne 0 ]; then
      echo "Error executing $FILE"; exit 1
    fi
  else
    echo "File not found: $FILE"
  fi
done
