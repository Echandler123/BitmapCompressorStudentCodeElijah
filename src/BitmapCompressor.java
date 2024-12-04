/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Elijah Chandler
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        final int MAX_REPEAT = 255;
        final int RUN_SIZE = 8;
        int repeat = 0;
        // Current bit
        boolean isOne = false;
        while(!BinaryStdIn.isEmpty()){
            // Read the next bit
            boolean bit = BinaryStdIn.readBoolean();
            // If the bit is different from the last bit or the repeat count is 255
            if(bit != isOne || repeat == MAX_REPEAT){
                if (repeat > MAX_REPEAT){
                    // Write the maximum repeat value
                    BinaryStdOut.write( MAX_REPEAT, RUN_SIZE);
                    // Write where the to continue the repeat
                    BinaryStdOut.write( 0, RUN_SIZE);
                    // Subtract the maximum repeat value from the repeat count
                    repeat =- MAX_REPEAT;
                }
                // Write the repeat count
                BinaryStdOut.write(repeat, RUN_SIZE);
                repeat = 0;
                isOne = bit;
            }
            // Increment the repeat count
            repeat++;
        }
        // Any extra repeats
        if(repeat > 0){
            // When the repeat count is greater than the maximum repeat value
            while(repeat > MAX_REPEAT){
                // Write the maximum repeat value
                BinaryStdOut.write(MAX_REPEAT,RUN_SIZE );
                BinaryStdOut.write(0,RUN_SIZE );
                repeat -= MAX_REPEAT;
            }
            // Write the remaining repeat count
            BinaryStdOut.write(repeat, RUN_SIZE);
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        boolean isOne = false;
        // While there are still bits to read
        while(!BinaryStdIn.isEmpty()) {
            // Read the repeat count
            int repeatCount = BinaryStdIn.readInt(8);
            // Write the bit the number of times it was repeated
            for(int i = 0; i < repeatCount; i++){
                BinaryStdOut.write(isOne);
            }
            // Switch the bit
            isOne = !isOne;
        }
        BinaryStdOut.close();
    }
    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}