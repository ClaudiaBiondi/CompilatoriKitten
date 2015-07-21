package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

/**
 * A list of Kitten bytecodes.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

// Implementa una lista di bytecode che puo essere inserita in un blocco di codice (la struttura che implementa tale blocco 
// e translation/Block.java

public class BytecodeList {

    /**
     * The bytecode contained at the head of this list.
     */

    private final Bytecode head;

    /**
     * The next instruction of this list of bytecodes.
     * It is {@code null} if this is the last element of the list.
     */

    private final BytecodeList tail;

    /**
     * Constructs a list of bytecodes that starts with the
     * given bytecode and continues with the given list of bytecodes.
     *
     * @param head the first bytecode of the list to be constructed
     * @param tail the list of bytecodes that follows {@code head}
     */

    public BytecodeList(Bytecode head, BytecodeList tail) {
    	this.head = head;
    	this.tail = tail;
    }

    /**
     * Constructs a list of bytecodes containing a single bytecode.
     *
     * @param head the only element of the list
     */

    public BytecodeList(Bytecode head) {
    	this(head, null);
    }

    /**
     * Yields the head of this list.
     *
     * @return the head of this list
     */

    public Bytecode getHead() {
    	return head;
    }

    /**
     * Yields the tail of this list.
     *
     * @return the tail of this list
     */

    public BytecodeList getTail() {
    	return tail;
    }

    /**
     * Computes the concatenation of this list of bytecodes and another. Intermediate
     * {@code nop} instructions are removed.
     *
     * @param other the list of bytecodes that must be appended after this
     * @return the result of the concatenation of this list of bytecodes with {@code other}.
     *         This list and {@code other} are not modified
     */

    public BytecodeList append(BytecodeList other) {
    	if (other == null)
    		return this;
    	else if (other.head instanceof NOP)
    		return append(other.tail);
    	else if (tail == null)
    		return new BytecodeList(head, other);
    	else
    		return new BytecodeList(head, tail.append(other));
    }

    @Override
    public String toString() {
    	String s = head.toString();
    	if (s.length() > 100)
    		s = s.substring(0,100) + "...";

    	if (tail != null)
    		// se campi bytecode sono stringhe vuote, non stampiamo nessuna new line
    		// puo essere utile per i bytecode che spariscono dal print-out
    		if (s.length() > 0)
    			return s + "\n" + tail.toString();
    		else
    			return tail.toString();
    	else
    		return s;
    }

    /**
     * Generates the Java bytecode corresponding to this list of bytecodes.
     * This just calls {@code bytecode.NonBranchingBytecode.generateJavaBytecode(JavaClassGenerator)} on each
     * non-branching bytecode in the list and appends the results.
     *
     * @param classGen the Java class generator to be used for this generation
     * @return the Java bytecode corresponding to this list of bytecodes
     */

    public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
    	InstructionList result;

    	if (head instanceof NonBranchingBytecode)
    		// generaimo il java bytecode dal primo bytecode
    		// se non e una condizione di un ramo 
    		
    		result = ((NonBranchingBytecode) head).generateJavaBytecode(classGen);
    	else
    		result = new InstructionList();

    	// per i successori 
    	if (tail != null)
    		result.append(tail.generateJavaBytecode(classGen));

    	// se non aggiungiamo nessuna istruzione , aggiungiamo una fittizia per non far ritornare mai una lista vuota
    	if (result.isEmpty())
    		result.append(new org.apache.bcel.generic.NOP());

    	return result;
    }
}