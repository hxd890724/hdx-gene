package org.example.cli;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Hexudong
 * @date 2024/12/24
 */
public class Login implements Callable<Integer> {

    /**
     * 默认字符分隔符
     */
    private static final String DEFAULT_SEPARATOR = ",";

    @Option(names = {"-u", "--user"}, description = "User Name")
    String user;

    @Option(names = {"-p", "--password"}, arity = "0..1", description = "Passphrase", interactive = true)
    String password;

    @Option(names = {"-cp", "--checkPassword"}, arity = "0..1", description = "Check Password", interactive = true)
    String checkPassword;

    @Override
    public Integer call() {
        System.out.println("password = " + password);
        System.out.println("checkPassword = " + checkPassword);
        return 0;
    }

    public static void main(String[] args) {
        String arg = "-u,hxd";
        String[] wholeArgs = suppleMissedOption(arg);
        int exitCode = new CommandLine(new Login()).execute(wholeArgs);
        System.exit(exitCode);
    }

    /**
     * 补全缺失的Option参数
     *
     * @param arg 输入的参数
     * @return 完整的参数
     */
    private static String[] suppleMissedOption(String arg) {
        String[] args = arg.split(DEFAULT_SEPARATOR);
        StringBuilder sb = new StringBuilder(arg);
        Class<?> loginClass = Login.class;
        Field[] declaredFields = loginClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 获取字段上的所有注解
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Option) {
                    Option option = (Option) annotation;
                    String[] names = option.names();
                    if (!Arrays.asList(args).contains(names[0]) && !Arrays.asList(args).contains(names[1])) {
                        sb.append(DEFAULT_SEPARATOR).append(names[0]);
                    }
                }
            }
        }
        return sb.toString().split(DEFAULT_SEPARATOR);
    }
}
