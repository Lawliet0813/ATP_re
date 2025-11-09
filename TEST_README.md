# ATP_re Testing Framework

This repository includes a comprehensive testing framework for RU (Recording Unit) decoder and MMI (Man-Machine Interface) components.

## Quick Links

- **[Full Testing Framework Documentation](./TESTING_FRAMEWORK.md)** - Complete testing guide
- **[Contributing to Tests](./CONTRIBUTING_TESTS.md)** - Guide for writing tests
- **[CI/CD Workflow](./.github/workflows/ru-mmi-regression.yml)** - Automated testing configuration

## Test Components

### 1. Unit Tests (JUnit 5)
- **Location**: `tests/java/`
- **Run**: `mvn test`
- **Coverage**: `mvn jacoco:report`

### 2. Regression Tests (Bash)
- **Script**: `test_batch.sh`
- **Run**: `./test_batch.sh`

### 3. CI/CD Integration
- **Workflow**: `.github/workflows/ru-mmi-regression.yml`
- **Triggers**: Push to main, Pull requests

## Quick Start

### Running Unit Tests

```bash
# Install Maven (if not already installed)
# Ubuntu/Debian: sudo apt-get install maven
# macOS: brew install maven

# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Running Regression Tests

```bash
# Make the script executable (if not already)
chmod +x ./test_batch.sh

# Run the tests
./test_batch.sh
```

### Configuration

The script can be configured using environment variables:

- `PROCESS_CMD`: Command to process RU log files (default: `./bin/atp_re process`)
- `TOLERANCE`: Test tolerance value (default: `0.01`)
- `TEST_DIR`: Directory containing test data files (default: `./test_data`)
- `RESULT_DIR`: Directory for test results (default: `./test_results`)

Example:

```bash
PROCESS_CMD="docker run atp_re process" TEST_DIR="./my_tests" ./test_batch.sh
```

## Adding Test Data

To add RU log files for testing:

1. Create the test data directory (if not exists):
   ```bash
   mkdir -p test_data
   ```

2. Place your RU log files in the `test_data` directory. Supported file extensions:
   - `.ru` - RU log files (Recording Unit binary log files)
   - `.log` - Log files (text or binary format)
   - `.dat` - Data files (raw data format)

   **File Format**: The test script expects RU log files following the ATPRU-LOGF-001 RU Log File Data Format specification (version 1.8). These files contain ATP (Automatic Train Protection) system recordings including:
   - RU record headers with timestamps, location stamps, and speed stamps
   - Data records for ATP/MMI, VDX, BTM telegrams, and other system data
   - See `RU_DECODER_UPDATE_NOTES.md` for detailed format specifications

3. Run the test script:
   ```bash
   ./test_batch.sh
   ```

## Test Results

Test results are stored in the `test_results` directory. Each test creates a result file with the format: `<test_file_name>.result`

## GitHub Actions Workflow

The workflow automatically runs on:
- Push to `main` branch
- Pull requests to `main` branch

The workflow:
1. Checks out the code
2. Sets up Python 3.10
3. Runs the batch tests using `test_batch.sh`

## Exit Codes

- `0`: All tests passed or no tests to run
- `1`: One or more tests failed

## Notes

- The script handles missing test data gracefully
- If the process binary doesn't exist, tests are skipped
- Test data and results directories are excluded from git via `.gitignore`
