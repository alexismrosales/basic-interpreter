import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interpreter {
    static boolean error_exist = false;
    public static void main(String[] args) throws IOException{
        if(args.length > 1) {
            System.out.println("Correct use is: interpreter [script]");
            System.exit(64);
        } else if(args.length == 1){
            execFile(args[0]);
        } else{
            execPrompt();
        }
    }
    private static void execFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        exec(new String(bytes, Charset.defaultCharset()));
        //If there is an error
        if(error_exist) System.exit(65);
    }
    private static void execPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String line = reader.readLine();
            if(line == null) break; // if is pressed ctrl+D
            exec(line);
            error_exist = false;
        }
    }
    private static void exec(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        /*for(Token token : tokens) {
            System.out.println(token);
        }*/
        Parser parser = new Parser(tokens);
        tokens = parser.parse();
        if(tokens == null)
        {
            System.out.println("ERROR");
        }
        else
        {
            PostfixGenerator gpf =new PostfixGenerator(tokens);
            List<Token> postfix = gpf.convertir();
            /*for(Token token : postfix) {
                System.out.println(token);
            }*/
            ASTGenerator gast = new ASTGenerator(postfix);
            Tree program = gast.generateAST();
            //program.print();
            program.traverse();
        }

    }
    static void error(int line, String message){
        report(line, "",message);
    }
    private static void report(int line, String where, String message){
        System.err.println("[Linea " + line+ "] Error" + where + ": " + message);
        error_exist = true;
    }
}
