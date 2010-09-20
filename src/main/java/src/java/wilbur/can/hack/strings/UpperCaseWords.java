package wilbur.can.hack.strings;

import com.linkedin.pig.SimpleEvalFunc;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.schema.Schema;

/** 
 * upper-case the first character of every word in a sentence
 */
public class UpperCaseWords extends EvalFunc<String> {
    @Override
    public String exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            String str = (String) input.get(0);
            if (str == null) return null;
            if (str.length() == 0) return str;

            // Split the sentence on space
            String[] inputWords = str.split(" ");
            String[] upperWords = new String[inputWords.length];

            // Upper case each word
            for(int i=0; i < inputWords.length; i++)
            {
               upperWords[i] = Character.toUpperCase(inputWords[i].charAt(0)) + inputWords[i].substring(1);
            }

            // Join upper case words on space and return
            String output = join(upperWords, " ");
            return output;
            
        } catch (ExecException e) {
            log.warn("Error reading input: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Schema outputSchema(Schema input) {
        return new Schema(new Schema.FieldSchema(null, DataType.CHARARRAY));
    }

    // Join array of strings by a delimeter 
    public static String join(String[] strings, String separator) {
    StringBuffer sb = new StringBuffer();
    for (int i=0; i < strings.length; i++) {
        if (i != 0) sb.append(separator);
  	    sb.append(strings[i]);
  	}
  	return sb.toString();
}
}