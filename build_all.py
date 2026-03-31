import os
import sys
import subprocess
import platform

# 1. Configuration
PRE_BUILD_SCRIPT = "pbr_redistributor.py"

# The exact order you specified
VERSIONS = [
    "1.20.1",
    "1.20.4",
    "1.20.6",
    "1.21.4",
    "1.21.1",
    "1.21.8",
    "1.21.11"
]

# The name of the property you change in gradle.properties to switch versions
PROPERTY_NAME = "active_mc_version"

def run_command(command, error_message):
    print(f"\n>>> Executing: {' '.join(command)}")
    result = subprocess.run(command)
    if result.returncode != 0:
        print(f"\n❌ ERROR: {error_message}")
        sys.exit(result.returncode)

def main():
    print("🚀 Starting Automated Multi-Version Build...")

    # 2. Run your existing Python prerequisite script
    if os.path.exists(PRE_BUILD_SCRIPT):
        print(f"\n--- Running Pre-Build Script ({PRE_BUILD_SCRIPT}) ---")
        run_command([sys.executable, PRE_BUILD_SCRIPT], "Pre-build script failed!")
    else:
        print(f"\n⚠️ Pre-build script '{PRE_BUILD_SCRIPT}' not found. Skipping.")

    # 3. Determine the correct Gradle wrapper for the OS
    is_windows = platform.system().lower() == "windows"
    gradlew = "gradlew.bat" if is_windows else "./gradlew"

    # 4. Loop through versions and build
    for version in VERSIONS:
        print(f"\n" + "="*50)
        print(f"🔨 BUILDING VERSION: {version}")
        print("="*50)

        # By using -P, we override the gradle.properties value dynamically!
        # Adjust 'runDatagen' if your datagen task is named differently (e.g., 'datagen')
        gradle_args = [
            gradlew,
            "runDatagen",
            "build",
            f"-P{PROPERTY_NAME}={version}"
        ]

        run_command(gradle_args, f"Build failed for version {version}!")

    print("\n✅ All versions built successfully! Check your build/libs folders.")

if __name__ == "__main__":
    main()