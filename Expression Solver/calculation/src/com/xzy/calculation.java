package com.xzy;

import java.util.Scanner;
import java.util.Stack;
import java.util.regex.*;//字符串匹配
import java.math.BigDecimal;

public class calculation {
    private static Stack<Character> OP = new Stack<>();
    private static Stack<BigDecimal> NUM = new Stack<>();
    private static int spoint;
    public static int errorFlag;
    public static final Pattern CharRange= Pattern.compile("[0-9\\.+-/*%^()#eE ]+");//使用正则表达式来保存合法的符号范围

    private static void initialize(){
        spoint=1;
        errorFlag=0;
        OP.clear();
        NUM.clear();
    }
    public static int checkSomeError(String expression){//处理空串/非算符/括号乱加/括号数不匹配的情况，直接报错
        Matcher islegalString = CharRange.matcher(expression);
        if(!islegalString.matches())return 3;//正则表达式判断非法字符

        if(expression.equals("##")||expression.trim().equals("##"))return 4;//对于空行或者删减后的空行价进行判断

        char []s=expression.toCharArray();
        Stack<Character> brkt= new Stack<>();
        for (char c : s) {//对 不成对 的括号进行判断
            if (c == '(')
                brkt.push(c);
            else if (c == ')') {
                if (!brkt.empty()&&brkt.peek() == '(') brkt.pop();
                else brkt.push(c);
            }
        }
        if(!brkt.empty())return 6;

        for(int i=0;i<=s.length-2;i++) {//对非法括号以及小数点位置进行判断
            if((s[i]=='('||s[i]=='.')&&
                (
                    s[i+1]==')'||s[i+1]=='/'||s[i+1]=='*'||s[i+1]=='%' ||s[i+1]=='^'||s[i+1]=='+'||s[i+1]=='.'||s[i+1]=='#'||
                            (s[i+1]=='-' && !('0'<=s[i+2]&&s[i+2]<='9') )
                )
            )return 5;
            if((s[i+1]==')'||s[i+1]=='.'||s[i+1]=='#')&&
                (
                    s[i]=='('||s[i]=='/'||s[i]=='*'||s[i]=='%' ||s[i]=='^'||s[i]=='+'||s[i]=='-'||s[i]=='.'||s[i]=='#'
                )
            )return 5;
        }
        for(int i=0;i<=s.length-3;i++){//对科学计数法中的错误给出提示
            if((s[i+1]=='e'||s[i+1]=='E')&& !('1'<=s[i]&&s[i]<='9'))
                return 1;
            if((s[i]=='e'||s[i]=='E')&&
                (
                    s[i+1]=='('||s[i+1]=='/'||s[i+1]=='*'||s[i+1]=='%' ||s[i+1]=='^' || s[i+1]=='+'||s[i+1]=='.'
                )
            )return 1;
        }
        return 0;
    }
    public static char cmpOP(char a,char b){
        if(a=='+' && b=='+')return '>';if(a=='+' && b=='-')return '>';if(a=='+' && b=='*')return '<';if(a=='+' && b=='/')return '<';if(a=='+' && b=='%')return '<';if(a=='+' && b=='^')return '<';if(a=='+' && b=='(')return '<';if(a=='+' && b==')')return '>';if(a=='+' && b=='#')return '>';
        if(a=='-' && b=='+')return '>';if(a=='-' && b=='-')return '>';if(a=='-' && b=='*')return '<';if(a=='-' && b=='/')return '<';if(a=='-' && b=='%')return '<';if(a=='-' && b=='^')return '<';if(a=='-' && b=='(')return '<';if(a=='-' && b==')')return '>';if(a=='-' && b=='#')return '>';
        if(a=='*' && b=='+')return '>';if(a=='*' && b=='-')return '>';if(a=='*' && b=='*')return '>';if(a=='*' && b=='/')return '>';if(a=='*' && b=='%')return '>';if(a=='*' && b=='^')return '<';if(a=='*' && b=='(')return '<';if(a=='*' && b==')')return '>';if(a=='*' && b=='#')return '>';
        if(a=='/' && b=='+')return '>';if(a=='/' && b=='-')return '>';if(a=='/' && b=='*')return '>';if(a=='/' && b=='/')return '>';if(a=='/' && b=='%')return '>';if(a=='/' && b=='^')return '<';if(a=='/' && b=='(')return '<';if(a=='/' && b==')')return '>';if(a=='/' && b=='#')return '>';
        if(a=='%' && b=='+')return '>';if(a=='%' && b=='-')return '>';if(a=='%' && b=='*')return '>';if(a=='%' && b=='/')return '>';if(a=='%' && b=='%')return '>';if(a=='%' && b=='^')return '<';if(a=='%' && b=='(')return '<';if(a=='%' && b==')')return '>';if(a=='%' && b=='#')return '>';
        if(a=='^' && b=='+')return '>';if(a=='^' && b=='-')return '>';if(a=='^' && b=='*')return '>';if(a=='^' && b=='/')return '>';if(a=='^' && b=='%')return '>';if(a=='^' && b=='^')return '<';if(a=='^' && b=='(')return '<';if(a=='^' && b==')')return '>';if(a=='^' && b=='#')return '>';
        if(a=='(' && b=='+')return '<';if(a=='(' && b=='-')return '<';if(a=='(' && b=='*')return '<';if(a=='(' && b=='/')return '<';if(a=='(' && b=='%')return '<';if(a=='(' && b=='^')return '<';if(a=='(' && b=='(')return '<';if(a=='(' && b==')')return '=';if(a=='(' && b=='#')return 'E';
        if(a==')' && b=='+')return '>';if(a==')' && b=='-')return '>';if(a==')' && b=='*')return '>';if(a==')' && b=='/')return '>';if(a==')' && b=='%')return '>';if(a==')' && b=='^')return '>';if(a==')' && b=='(')return 'E';if(a==')' && b==')')return '>';if(a==')' && b=='#')return '>';
        if(a=='#' && b=='+')return '<';if(a=='#' && b=='-')return '<';if(a=='#' && b=='*')return '<';if(a=='#' && b=='/')return '<';if(a=='#' && b=='%')return '<';if(a=='#' && b=='^')return '<';if(a=='#' && b=='(')return '<';if(a=='#' && b==')')return 'E';if(a=='#' && b=='#')return '=';
        return 'E';
    }
    private static char getElem(char[] s){
        if(spoint==s.length)return '#';

        if('0'<=s[spoint]&&s[spoint]<='9'){
            int mark=spoint;
            StringBuilder curNumString = new StringBuilder(10);
            if(spoint>=2&&OP.peek()=='-'&& !('0'<=s[spoint-2]&&s[spoint-2]<='9') ){//负号情况
                curNumString.append('-');
                OP.pop();
            }
            while(mark<= s.length-2&&( ('0'<=s[mark]&&s[mark]<='9')||(s[mark]=='.')||s[mark]=='e'||s[mark]=='E' )) {
                if(s[mark]=='e'||s[mark]=='E'){
                    curNumString.append(s[mark]);
                    mark++;
                    if(mark<=s.length-2&&s[mark]=='-'){
                        curNumString.append(s[mark]);
                        mark++;
                    }
                    continue;
                }
                curNumString.append(s[mark]);
                mark++;
            }
            spoint=mark;
            NUM.push(new BigDecimal(curNumString.toString())) ;
            return 'N';
        }
        //剩下为字符情况
        spoint++;
        return s[spoint-1];
    }
    public static int checkCal(char opt,BigDecimal y){
        if(opt=='/'&&y.equals(new BigDecimal(0)))return 2;
        if(opt=='^'&&y.intValue()<0)return 7;
        return 0;
    }
    public static BigDecimal operate(BigDecimal x,char opt,BigDecimal y){
        BigDecimal result = new BigDecimal(0);
        switch (opt) {
            case '+' -> result = x.add(y);
            case '-' -> result = x.subtract(y);
            case '*' -> result = x.multiply(y);
            case '/' -> result = x.divide(y, 10, BigDecimal.ROUND_HALF_DOWN);
            case '%' -> {
                BigDecimal[] modtmp = x.divideAndRemainder(y);
                result = modtmp[1];
            }
            case '^' -> result = x.pow(y.intValue());
        }
        return result;
    }
    public static String translateRedex(BigDecimal number,int redex){
        if(new BigDecimal(number.longValue()).compareTo(number)==0){//整数
            long tmp = number.longValueExact();
            switch (redex){
                case 2->{return Long.toBinaryString(tmp);}
                case 16->{return Long.toHexString(tmp);}
                default -> {return Long.toOctalString(tmp);}
            }
        }else{//浮点数
            long tmp = Double.doubleToRawLongBits(number.doubleValue());
            switch (redex){
                case 2->{return Long.toBinaryString(tmp);}
                case 16->{return Long.toHexString(tmp);}
                default -> {return Long.toOctalString(tmp);}
            }
        }
    }
    public static void handleExpression(String expression){
        initialize();
        errorFlag=checkSomeError(expression);//若出错会改变errorFlag的值
        char c='#';
        char[] s=expression.toCharArray();
        if(errorFlag==0){
            OP.push('#');
            c=getElem(s);
        }
        while(errorFlag==0&&(c!='#'||OP.peek()!='#')){
            if(c=='N'){
                c=getElem(s);
            }else switch (cmpOP(OP.peek(), c)) {
                case '<' -> {
                    OP.push(c);
                    c = getElem(s);
                }
                case '=' -> {
                    OP.pop();
                    c = getElem(s);
                }
                case '>' -> {
                    if(spoint>=2&&c=='-'&& !('0'<=s[spoint-2]&&s[spoint-2]<='9') ) {//[op][-][num],此时【-】不参与运算而寻求与后面数字结合
                            OP.push(c);//先压进去负号
                            c=getElem(s);
                            break;
                    }
                    char opt = OP.peek();
                    OP.pop();
                    BigDecimal x=new BigDecimal(0);
                    BigDecimal y=new BigDecimal(0);
                    if(NUM.size()>1){
                        y = NUM.peek();
                        NUM.pop();
                        x = NUM.peek();
                        NUM.pop();
                        errorFlag = checkCal(opt, y);
                    }else errorFlag = 81;
                    if (errorFlag == 0) {
                        NUM.push(operate(x, opt, y));
                    }
                }
                case 'E' -> errorFlag = 1;//表达式有错
            }
        }
        if(NUM.size()>1||OP.size()>1)errorFlag=1;
        if(errorFlag==0){
            System.out.println("OCT: "+NUM.peek());
            System.out.println("BIN: "+translateRedex(NUM.peek(),2));
            System.out.println("HEX: "+translateRedex(NUM.peek(),16));
        }else{
            switch (errorFlag) {
                case 7 -> System.out.println("Negative Exp!\n");
                case 6 -> System.out.println("Unmatched Bracket Pair!\n");
                case 5 -> System.out.println("Character's Position is Wrong!\n");
                case 4 -> System.out.println("Void Expression!\n");
                case 3 -> System.out.println("illegal character!\n");
                case 2 -> System.out.println("divide 0!\n");
                case 1 -> System.out.println("Error expression!\n");
                default  -> System.out.println("Unknown Error!\n");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Enter '#' to quit :)");
        System.out.println("please enter the expression:");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while(!"#".equals(userInput)){
            handleExpression('#'+userInput+'#');
            System.out.println("please enter the expression:");
            userInput = scanner.nextLine();
        }
    }
}
