package uddchecker.editors;

import org.eclipse.jface.text.rules.*;

public class XMLPartitionScanner extends RuleBasedPartitionScanner {
	public final static String XML_COMMENT = "__xml_comment";
	public final static String XML_TAG = "__xml_tag";
	public final static String ACCESS_CHECK_ERROR = "__access_check_error";

	public XMLPartitionScanner() {

		IToken xmlComment = new Token(XML_COMMENT);
		IToken tag = new Token(XML_TAG);
		IToken accessCheckError = new Token(ACCESS_CHECK_ERROR);

		IPredicateRule[] rules = new IPredicateRule[3];

		rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
		rules[1] = new TagRule(tag);
		rules[2] = new AccessCheckRule(accessCheckError);
		

		setPredicateRules(rules);
	}
}
