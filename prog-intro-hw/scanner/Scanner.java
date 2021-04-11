package scanner;

import java.io.*;

public class Scanner implements AutoCloseable {
    private final int MIN_BUFFER_SIZE = 256;
    private Reader reader;
    private char[] buf = new char[MIN_BUFFER_SIZE];
    private int writePosition = 0;
    private int readPosition = 0;
    private int scanPosition = 0;
    private boolean endOfInput = false;

    public Scanner(InputStream inputStream, String charsetName) {
        try {
            reader = new InputStreamReader(inputStream, charsetName);
        } catch (IOException e) {
            reader = null;
            endOfInput = true;
        }
    }

    public Scanner(String inputStr, String charsetName) {
        try {
            reader = new InputStreamReader(new ByteArrayInputStream(inputStr.getBytes(charsetName)), charsetName);
        } catch (IOException e) {
            reader = null;
            endOfInput = true;
        }
    }

    public Scanner(File inputFile, String charsetName) {
        try {
            reader = new InputStreamReader(new FileInputStream(inputFile), charsetName);
        } catch (IOException e) {
            reader = null;
            endOfInput = true;
        }
    }

    private void allocate() {
        if (scanPosition == 0) {
            char[] nbuf = new char[buf.length * 2];
            System.arraycopy(buf, 0, nbuf, 0, writePosition);
            buf = nbuf;
        } else {
            System.arraycopy(buf, scanPosition, buf, 0, writePosition - scanPosition);
            readPosition -= scanPosition;
            writePosition -= scanPosition;
            scanPosition = 0;
        }
    }

    private void read() {
        if (endOfInput) {
            return;
        }
        if (readPosition == buf.length) {
            allocate();
        }
        int countOfReaded;
        try {
            countOfReaded = reader.read(buf, writePosition, buf.length - writePosition);
        } catch (IOException e) {
            countOfReaded = -1;
        }
        if (countOfReaded == -1) {
            endOfInput = true;
            countOfReaded = 0;
        }
        writePosition += countOfReaded;
    }

    private String nextToString() {
        String str = new String(buf, scanPosition, readPosition - scanPosition);
        scanPosition = readPosition;
        return str;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            reader = null;
        } finally {
            endOfInput = true;
        }
    }

    public boolean hasNext() {
        while (scanPosition < writePosition && Character.isWhitespace(buf[scanPosition])) {
            scanPosition++;
        }
        readPosition = scanPosition;
        while (true) {
            while (readPosition < writePosition && !Character.isWhitespace(buf[readPosition])) {
                readPosition++;
            }
            if (scanPosition == writePosition && endOfInput) {
                return false;
            }
            if (readPosition == writePosition && !endOfInput) {
                read();
            } else {
                return true;
            }
        }
    }

    public String next() {
        if (!hasNext()) {
            return null;
        }
        return nextToString();
    }

    public boolean hasNextInt() {
        boolean returnValue = false;
        if (hasNext()) {
            int savedScanPosition = scanPosition;
            try {
                Integer.parseInt(next());
                returnValue = true;
            } catch (NumberFormatException ignored) {
            }
            scanPosition = savedScanPosition;
        }
        return returnValue;
    }

    public Integer nextInt() {
        if (!hasNextInt()) {
            return null;
        }
        return Integer.parseInt(next());
    }

    public boolean hasNextLine() {
        while (true) {
            readPosition = scanPosition;
            while (readPosition < writePosition && buf[readPosition] != '\n' && buf[readPosition] != '\r') {
                readPosition++;
            }
            if (scanPosition >= writePosition && endOfInput) {
                return false;
            }
            if ((readPosition >= writePosition
                    || (readPosition + 1 == writePosition && buf[readPosition] == '\r')) && !endOfInput) {
                read();
            } else {
                return true;
            }
        }
    }

    public String nextLine() {
        if (!hasNextLine()) {
            return null;
        }
        String str = nextToString();
        scanPosition++;
        if (scanPosition < writePosition && buf[scanPosition - 1] == '\r' && buf[scanPosition] == '\n') {
            scanPosition++;
        }
        return str;
    }
}
