package uddchecker.editors;

import org.eclipse.jface.text.rules.*;

public class AccessCheckRule extends MultiLineRule {

	public AccessCheckRule(IToken token) {
		super("<accessCheck", "/>", token);
	}
	protected boolean sequenceDetected(
		ICharacterScanner scanner,
		char[] sequence,
		boolean eofAllowed) 
	{
		System.out.println(sequence);
		int c = scanner.read();
		if (sequence[0] == '<') {
			if (c != 'n') {
				// processing instruction - abort
				scanner.unread();
				return false;
			}
		} /*else if (sequence[0] == '>') {
			scanner.unread();
		}*/
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}
}