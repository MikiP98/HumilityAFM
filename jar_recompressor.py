# coding=utf-8
import os
import shutil
import zipfile
import zopfli.zlib

from multiprocessing import Pool, cpu_count


INPUT_DIR = "./build/libs"
BACKUP_EXT = ".bak"


def recompress_jar(jar_path: str) -> None:
    backup_path = jar_path + BACKUP_EXT
    try:
        print(f"[PID {os.getpid()}] Recompressing: {jar_path}")

        # Make a backup first
        shutil.copy2(jar_path, backup_path)

        # Open the old jar
        with zipfile.ZipFile(backup_path, "r") as zin:
            entries = zin.infolist()

            # Write a new jar with Zopfli recompression
            with zipfile.ZipFile(jar_path, "w", compression=zipfile.ZIP_DEFLATED) as zout:
                for entry in entries:
                    data = zin.read(entry.filename)

                    # Skip directories
                    if entry.is_dir():
                        zout.writestr(entry, b"")
                        continue

                    # Use Zopfli for maximum compression
                    compressed = zopfli.zlib.compress(data)

                    # Preserve metadata
                    new_entry = zipfile.ZipInfo(entry.filename)
                    new_entry.date_time = entry.date_time
                    new_entry.external_attr = entry.external_attr
                    new_entry.compress_type = zipfile.ZIP_DEFLATED

                    zout.writestr(new_entry, compressed)

        print(f"[PID {os.getpid()}] ✔ Done: {jar_path} (backup at {backup_path})")
    except Exception as e:
        print(f"[PID {os.getpid()}] ❌ Failed: {jar_path} ({e})")


def main() -> None:
    jars = [
        os.path.join(INPUT_DIR, f)
        for f in os.listdir(INPUT_DIR)
        if f.endswith(".jar") and not f.endswith(".bak.jar") and not os.path.exists(os.path.join(INPUT_DIR, f + ".bak"))
    ]

    if not jars:
        print("No .jar files found in", INPUT_DIR)
        return

    print(f"Found {len(jars)} jars. Using up to {cpu_count()} workers.")

    with Pool(min(len(jars), cpu_count())) as pool:
        pool.map(recompress_jar, jars)


if __name__ == "__main__":
    main()
