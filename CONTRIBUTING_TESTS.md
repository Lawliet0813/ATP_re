# Contributing to ATP_re Testing

Thank you for contributing to the ATP_re testing framework! This guide will help you write effective tests.

## Quick Start

1. **Set up your environment**
   ```bash
   # Install Maven
   sudo apt-get install maven  # Ubuntu/Debian
   brew install maven          # macOS
   
   # Verify installation
   mvn --version
   ```

2. **Run existing tests**
   ```bash
   mvn test
   ```

3. **View coverage report**
   ```bash
   mvn jacoco:report
   open target/site/jacoco/index.html
   ```

## Writing Unit Tests

### Test File Location

Place test files in `tests/java/` matching the package structure:
```
tests/java/
└── decoder/
    ├── RUDecoderTest.java
    ├── MMIDecoderTest.java
    └── BTMDecoderTest.java
```

### Test Template

```java
package decoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class MyTest {
    private MyClass instance;
    
    @BeforeEach
    public void setUp() {
        instance = new MyClass();
    }
    
    @Test
    @DisplayName("Should do something correctly")
    public void testSomething() {
        // Given
        byte[] input = new byte[]{0x01, 0x02};
        
        // When
        Object result = instance.process(input);
        
        // Then
        assertNotNull(result);
        assertEquals(expected, result);
    }
}
```

## Test Guidelines

### DO:
✅ Write descriptive test names
✅ Test edge cases (null, empty, max values)
✅ Use @DisplayName for clarity
✅ Follow AAA pattern (Arrange, Act, Assert)
✅ Keep tests independent
✅ Test one thing per test method
✅ Use meaningful assertion messages

### DON'T:
❌ Test implementation details
❌ Create interdependent tests
❌ Use hard-coded paths
❌ Ignore failing tests
❌ Skip writing tests for bug fixes

## Coverage Goals

| Module | Target Coverage |
|--------|----------------|
| RU Decoder | 95% |
| MMI Decoder | 95% |
| BTM Decoder | 95% |
| Core Logic | 90% |
| Overall | 80% |

## Testing Different Components

### Decoder Tests
Test packet parsing, data extraction, and error handling:
```java
@Test
public void testPacketParsing() {
    byte[] packet = createValidPacket();
    RU ru = new RU(packet);
    
    assertEquals(1, ru.getPacketType());
    assertNotNull(ru.getTimestamp());
}
```

### Error Handling Tests
Verify graceful failure:
```java
@Test
public void testInvalidInput() {
    assertThrows(IllegalArgumentException.class, () -> {
        new RU(new byte[0]);
    });
}
```

### Edge Case Tests
Cover boundary conditions:
```java
@Test
public void testMaximumSpeed() {
    byte[] packet = createPacketWithSpeed(Integer.MAX_VALUE);
    RU ru = new RU(packet);
    
    assertTrue(ru.getSpeed() <= MAX_VALID_SPEED);
}
```

## Adding Test Data

### RU Test Files
1. Place files in `test_data/RU_file/`
2. Use `.RU` extension
3. Follow ATPRU-LOGF-001 v1.8 format
4. Include various packet types

### MMI Test Files
1. Place files in `test_data/MMI_file/`
2. Include multiple packet types
3. Test both valid and edge cases

## Running Specific Tests

```bash
# Run single test class
mvn test -Dtest=RUDecoderTest

# Run single test method
mvn test -Dtest=RUDecoderTest#testPacketParsing

# Run tests matching pattern
mvn test -Dtest=*DecoderTest

# Skip tests
mvn install -DskipTests
```

## Debugging Tests

### In IDE
1. Set breakpoints in test method
2. Run test in debug mode
3. Inspect variables and execution flow

### With Maven
```bash
# Run with debug output
mvn test -X

# Run with specific logging
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

## CI/CD Integration

Tests run automatically on:
- Push to `main` branch
- Pull requests to `main`

View results:
1. Go to Actions tab in GitHub
2. Select workflow run
3. Download test artifacts

## Troubleshooting

### Problem: Tests not found
**Solution**: Ensure test files:
- End with `Test.java`
- Are in `tests/java/` directory
- Have proper package declarations

### Problem: Compilation errors
**Solution**: 
- Check Java version (requires JDK 8+)
- Verify dependencies in `pom.xml`
- Run `mvn clean compile`

### Problem: Coverage not generated
**Solution**:
- Run `mvn clean test jacoco:report`
- Check JaCoCo plugin in `pom.xml`
- Verify tests actually ran

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Testing Best Practices](https://github.com/goldbergyoni/javascript-testing-best-practices)
- [ATPRU-LOGF-001 Specification](./RU_DECODER_UPDATE_NOTES.md)

## Getting Help

- Create issue with `testing` label
- Ask in pull request comments
- Reference [TESTING_FRAMEWORK.md](./TESTING_FRAMEWORK.md)

## Example: Complete Test

```java
package decoder;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RU Decoder Tests")
public class RUDecoderTest {
    
    private RU decoder;
    
    @BeforeEach
    public void setUp() {
        decoder = new RU();
    }
    
    @AfterEach
    public void tearDown() {
        decoder = null;
    }
    
    @Test
    @DisplayName("Should parse valid RU packet correctly")
    public void testValidPacket() {
        // Given
        byte[] packet = createValidRUPacket();
        
        // When
        decoder.parse(packet);
        
        // Then
        assertAll(
            () -> assertEquals(1, decoder.getPacketType()),
            () -> assertNotNull(decoder.getTimestamp()),
            () -> assertTrue(decoder.getSpeed() >= 0),
            () -> assertTrue(decoder.getLocation() >= 0)
        );
    }
    
    @Test
    @DisplayName("Should throw exception for null packet")
    public void testNullPacket() {
        assertThrows(NullPointerException.class, 
            () -> decoder.parse(null),
            "Should throw NPE for null input"
        );
    }
    
    @Test
    @DisplayName("Should handle empty packet")
    public void testEmptyPacket() {
        byte[] empty = new byte[0];
        
        assertThrows(IllegalArgumentException.class,
            () -> decoder.parse(empty),
            "Should throw IAE for empty packet"
        );
    }
    
    private byte[] createValidRUPacket() {
        byte[] packet = new byte[32];
        packet[0] = 1; // MMI_DYNAMIC
        // ... initialize other fields ...
        return packet;
    }
}
```

Happy Testing! 🧪
