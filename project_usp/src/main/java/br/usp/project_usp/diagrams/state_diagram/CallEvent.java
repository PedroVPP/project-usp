/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagrams.state_diagram;

import br.usp.project_usp.tests_construction.TestConstructor;
import br.usp.project_usp.tests_construction.VariablesHolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author pedro
 */
public class CallEvent {

    private String id;
    private String name;
    private List<Parameter> parameters;

    public CallEvent() {
        parameters = new ArrayList<Parameter>();
    }

    public CallEvent(String id, String name) {
        this.id = id;
        this.name = name;
        parameters = new ArrayList<Parameter>();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parameters
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    @Override
    public String toString() {
        String string = this.name + "(";
        
        List<Parameter> parameters = this.getParameters();
        for (Iterator<Parameter> it = parameters.iterator(); it.hasNext();) {
            Parameter parameter = it.next();
            string += parameter.getName();
            if(it.hasNext()) {
                string += ", ";
            }
        }
        
        string += ")";
        return string;
    }
    
    public String getParametersValues() { //minus the first one
        String result = "";
        for(int i = 1; i<this.getParameters().size(); i++) {
            String value = this.getParameters().get(i).getValue();
            result += value + " : ";
        }
        return result;
    }

    public void writeEventCode(TestConstructor testConstructor) throws IOException {

        String uriParameter = this.getParameters().get(0).getName();
        List<String> matchList = new ArrayList<String>();

        Pattern regex = Pattern.compile("\\{([^}]*)\\}"); //http://stackoverflow.com/questions/3684900/how-do-i-extract-words-in-braces-using-regular-expressions
        Matcher regexMatcher = regex.matcher(this.getParameters().get(0).getName());
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }

        for (Iterator<String> it = matchList.iterator(); it.hasNext();) {
            String variable = it.next();
            VariablesHolder.addVariable(variable, JOptionPane.showInputDialog(null, "Variable \""+ variable + "\" found. Please insert its value."));
            String modifiedUri = this.getParameters().get(0).getName().replaceFirst(regex.toString(), VariablesHolder.getVariableValueByName(variable));
            this.getParameters().get(0).setName("http://localhost:8080/"+modifiedUri);
        }
        

        if (this.name.equals("POST")) {
            for (int i = 1; i < getParameters().size(); i++) {
                Parameter parameter = getParameters().get(i);
                if(parameter.getValue() == null) {
                    parameter.setValue(JOptionPane.showInputDialog(null, "Variable \""+ parameter.getName() +"\" found. Please insert its value"));
                }
            }
            testConstructor.addLine(
                    "ContentProducer cp;\n"+
		"cp = new ContentProducer() {\n"+
			"public void writeTo(OutputStream outstream) {\n"+
            "\n"+
	"			try {\n"+
					"Writer writer = new OutputStreamWriter(outstream, \"UTF-8\");\n"+
					"writer.write(\""+this.getParametersValues()+"\");\n"+
					"writer.flush();\n"+
				"} catch (IOException e) {\n"+
					"e.printStackTrace();\n"+
				"}\n"+
			"}\n"+
		"};\n"+
		"HttpEntity entity = new EntityTemplate(cp);\n"+
		"HttpPost httppost = new HttpPost(\""+this.getParameters().get(0).getName()+"\");\n"+
		"httppost.setEntity(entity);\n"+
		"HttpClient httpClient = new DefaultHttpClient();\n"+
		"try {\n"+
			"HttpResponse result = httpClient.execute(httppost);\n"+
			"assertTrue(result.getStatusLine().getStatusCode(), 200);\n"+
		"} catch (Exception e) {\n"+
			"e.printStackTrace();\n"+
		"}\n"
        );
            
            
            
            
        } else if (this.name.equals("PUT")) {
            for (int i = 1; i < getParameters().size(); i++) {
                Parameter parameter = getParameters().get(i);
                if(parameter.getValue() == null) {
                    parameter.setValue(JOptionPane.showInputDialog(null, "Variable \""+ parameter.getName() +"\" found. Please insert its value"));
                }
            }
            
            testConstructor.addLine(
            "ContentProducer cp;\n"+
		"cp = new ContentProducer() {\n"+
			"public void writeTo(OutputStream outstream) {\n"+
				"try {\n"+
					"Writer writer = new OutputStreamWriter(outstream, \"UTF-8\");\n"+
					"writer.write(\""+this.getParametersValues()+"\");\n"+
					"writer.flush();\n"+
				"} catch (IOException e) {\n"+
					"e.printStackTrace();\n"+
				"}\n"+
			"}\n"+
		"};\n"+
		"\n"+
		"HttpEntity entity = new EntityTemplate(cp);\n"+
		"HttpPut putMethod = new HttpPut(\""+this.getParameters().get(0).getName()+"\");\n"+
		"putMethod.setEntity(entity);\n"+
		"HttpClient httpClient = new DefaultHttpClient();\n"+
		"try {\n"+
			"HttpResponse result = httpClient.execute(putMethod);\n"+
			"assertTrue(result.getStatusLine().getStatusCode(), 200);\n"+
		"} catch (Exception e) {\n"+
			"e.printStackTrace();\n"+
		"}"
            );
            
            
            
        } else if (this.name.equals("DELETE")) {
            testConstructor.addLine("//delete operation");
            testConstructor.addLine("HttpDelete deleteMethod = new HttpDelete(\"" + this.getParameters().get(0).getName() + "\");");
            testConstructor.addLine(
            
            "HttpClient httpClient = new DefaultHttpClient();\n" +
		"try {\n" +
			"HttpResponse result = httpClient.execute(deleteMethod);\n" +
                        "assertTrue(result.getStatusLine().getStatusCode(),204);\n" +
			"\n" +
		"} catch (Exception e) {\n" +
			"e.printStackTrace();\n" +
		"}\n"
            );
            testConstructor.addLine("//end of delete operation");
        } else {
        }
    }
}
