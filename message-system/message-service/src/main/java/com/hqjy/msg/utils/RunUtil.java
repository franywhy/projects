package com.hqjy.msg.utils;

import com.hqjy.msg.util.StringUtils;
import com.sun.tools.javac.Main;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RunUtil {
    private  static Logger logger = LoggerFactory.getLogger(RunUtil.class);
    @Value("${compile.path}")
    private String path;
    public static void main(String[] args) {

    }
    private static boolean isLoadJar =false;

    /**
     *
     * @param code 实现核心代码
     * @param clz 类名
     * @param method 执行方法
     * @return
     * @throws Exception
     */
    private static   synchronized Object compile2(String code,String clz,String method) throws Exception {
        String classPath = System.getProperty("user.dir")+ "/java/"; //存放java源文件的路径
        File sysFile = new File(classPath);
        if (!sysFile.exists()) {
            sysFile.mkdir();
        }
        File file = File.createTempFile(clz, ".java", sysFile);
        file.deleteOnExit();
        String className = getBaseFileName(file);
        String sourceStr = getClassCode(code,className,clz);

        //      将代码输出到文件
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        out.println(sourceStr);
        out.close();

        //
        //指定class路径，默认和源代码路径一致，加载class
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:" + classPath)});
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        JavaCompiler cmp = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fm = cmp.getStandardFileManager(null,null,null);
        JavaFileObject javaFileObject = new StringJavaObject(className,sourceStr);
        List<String> optionsList = new ArrayList<String>();

        // 编译文件的存放地方，注意：此处是为Eclipse 工具特设的
        System.out.println(classPath);

        optionsList.addAll(Arrays.asList("-d",classPath));
        List<File> dependencies = new ArrayList<File>();
        // 加载依赖的jar文件
        dependencies.addAll(getJarFiless(urlClassLoader,classLoader,Arrays.asList("gson-2.8.0.jar"/*,"message-core-1.0.jar","message-service-1.0.jar","persistence-api-1.0.jar"*/)));
        //getJarFiless(Arrays.asList("gson-2.8.0.jar"/*,"message-core-1.0.jar","message-service-1.0.jar","persistence-api-1.0.jar"*/));
        // 加载依赖的class文件
        //dependencies.addAll(getClassesFiles(Arrays.asList("com/hqjy/msg/model/MsgMessage.class")));
        fm.setLocation(StandardLocation.CLASS_PATH, dependencies);

        // 要编译的单元
        //List<JavaFileObject> jfos = Arrays.asList(javaFileObject);
        // 设置编译环境
       // JavaCompiler.CompilationTask task = cmp.getTask(null, fm, null,optionsList,null,jfos);
        Iterable it = fm.getJavaFileObjects(classPath+file.getName());
        JavaCompiler.CompilationTask task = cmp.getTask(null, fm, null, null, null, it);

        // 编译成功
        if(task.call()){

            try {

                //ClassLoader classLoader = fileManager.getClassLoader(null);
                Class cls = urlClassLoader.loadClass(className);
                Object handler = cls.newInstance();
                Method main = cls.getDeclaredMethod(method, null);
                Object object = main.invoke(handler, null);
                //deleteFile(classPath,className);
               /* deleteFile(System.getProperty("user.dir")
                        + "\\java\\",className);*/
                return object;
            } catch (Exception se) {
                se.printStackTrace();
            }finally {

            }
        }
        return  null;
    }

    private static void addJarToMemory(ClassLoader urlClassLoader,URLClassLoader classLoader,File jarFile) throws NoSuchMethodException {
        if (jarFile != null) {
            // 从URLClassLoader类中获取类所在文件夹的方法
            // 对于jar文件，可以理解为一个存放class文件的文件夹
            Method method = urlClassLoader.getClass().getDeclaredMethod("addURL", URL.class);
            boolean accessible = method.isAccessible();     // 获取方法的访问权限
            try {
                if (accessible == false) {
                    method.setAccessible(true);     // 设置方法的访问权限
                }
                // 获取系统类加载器
                //URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

                    URL url = jarFile.toURI().toURL();
                    try {
                        method.invoke(classLoader, url);
                        logger.debug("读取jar文件[name={}]", jarFile.getName());
                    } catch (Exception e) {
                        logger.error("读取jar文件[name={}]失败", jarFile.getName());
                    }

            }catch (Exception e){}
            finally {
                method.setAccessible(accessible);
            }
        }
    }

    private static String  getRootPath(){
        String path =RunUtil.class.getResource("/").getPath();
        //path = path.substring(1,path.length());
        return path;
    }



    private  List<File> getClassesFiles(URLClassLoader classLoader,List<String> strs) throws Exception {
        List<File> dependencyClasses = new ArrayList<File>();
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        for (int i = 0; i < strs.size(); i++) {
            String str =  strs.get(i);
            //Resource resource = new ClassPathResource("/BOOT-INF/classes/lib/" + str);
            //InputStream stream = resource.getInputStream();
            InputStream stream = classLoader.getResourceAsStream(str);
            if (null!=stream  ) {
                File targetFile = new File(str);
                FileUtils.copyInputStreamToFile(stream, targetFile);
                dependencyClasses.add(targetFile);
                //addJarToMemory(,classLoader,targetFile);
            }

        }
        System.out.println("class:"+dependencyClasses.size());
        return dependencyClasses;
    }

    private  static List<File> getJarFiless(URLClassLoader urlClassLoader,URLClassLoader classLoader,List<String> strs) throws Exception {
        List<File> dependencyJars = new ArrayList<File>();
        //String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        //String path = classLoader.getResource("").getPath();
        //System.out.println("path:"+path);
        for (int i = 0; i < strs.size(); i++) {
            String str =  strs.get(i);
            //Resource resource = new ClassPathResource("/BOOT-INF/classes/lib/" + str);
            //InputStream stream = resource.getInputStream();
            InputStream stream = ClassUtils.getDefaultClassLoader().getResourceAsStream("lib/"+str);
            if (null!=stream  ) {
                File targetFile = new File(str);
                FileUtils.copyInputStreamToFile(stream, targetFile);
                System.out.println("file:"+targetFile.getName()+"#"+targetFile.getAbsolutePath());
                dependencyJars.add(targetFile);
                if (!isLoadJar) {
                    addJarToMemory(urlClassLoader,classLoader,targetFile);
                    logger.info("add jar is success..");
                    isLoadJar = true;
                }

            }

        }
        System.out.println("jar:"+dependencyJars.size());
        return dependencyJars;
    }

    private synchronized  File compile(String code,String clz) throws Exception {
        File sysFile = new File(System.getProperty("user.dir")+"\\java");
        if (!sysFile.exists()) {
            sysFile.mkdir();
        }
        File file = File.createTempFile(clz, ".java", sysFile);
        file.deleteOnExit();
        //      获得类名
        String classname = getBaseFileName(file);
        //      将代码输出到文件
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        out.println(getClassCode(code, classname,clz));
        out.close();
        //      编译生成的java文件
        /*String[] cpargs = new String[] { "-d",
                System.getProperty("user.dir") +path+ "\\classes",
               *//* System.getProperty("user.dir")+"\\java\\"+file.getName(),*//*
                "-classpath",System.getProperty("user.dir") +path+ "\\classes"};
        int status = Main.compile(cpargs,out)*//*.compile(cpargs)*//*;*/
     String[] cpargs = new String[] { "-d",
                System.getProperty("user.dir") +path+ "\\classes\\",
               System.getProperty("user.dir")+"\\java\\"+file.getName(),
               "-cp",
             System.getProperty("user.dir")
                     + path+"\\gson-2.8.0.jar"
               /* "-classpath",System.getProperty("user.dir") +path+ "\\classes"*/};
        int status = Main.compile(cpargs);//*.compile(cpargs)*//*;*/
        //boolean status = compileFile(System.getProperty("user.dir")+"\\java\\"+file.getName(),System.getProperty("user.dir") +path+ "\\classes");

        if (status!=0 ) {
            throw new Exception("语法错误！");
        }

        return file;
    }
    public   synchronized Object run(String code,String methodName,String clzName) throws Exception {
        String classname = getBaseFileName(compile(code,clzName));
        File file = new File(System.getProperty("user.dir")
                + path+"\\classes\\" + classname + ".class");
                file.deleteOnExit();
        try {
            Class cls = Class.forName(classname);
            Object handler = cls.newInstance();
            Method main = cls.getDeclaredMethod(methodName, null);
            Object object = main.invoke(handler, null);
            deleteFile(System.getProperty("user.dir")
                    +path +"\\classes",classname);
            deleteFile(System.getProperty("user.dir")
                    + "\\java",classname);
            return object;
        } catch (Exception se) {
            se.printStackTrace();
        }finally {

        }
        return null;
    }

    private static void deleteFile(String path,String className){
        File file = new File(path);
        if (null != file && file.exists()) {
            if (file.isDirectory()) {
                Arrays.stream(file.listFiles()).forEach(f->{
                    deleteFile(f.getAbsolutePath(),className);
                });
            }else {
                if (file.getName().contains(className)) {
                    file.delete();
                }
            }
        }
    }
    private static String getClassCode(String code, String className,String clz) {
        StringBuffer text = new StringBuffer();

        //text.append("package com.hqjy.msg; import com.hqjy.msg.model.MsgMessage;import  com.hqjy.msg.util.*;import com.google.gson.internal.LinkedTreeMap;import java.util.function.Predicate;import java.util.*;");
        text.append("import java.util.function.Predicate;import java.util.Comparator;import java.util.Date;import com.google.gson.internal.LinkedTreeMap;import com.google.gson.Gson;import java.util.concurrent.ConcurrentHashMap;import java.util.Map;import java.util.HashMap;"/*import java.util.Map;import java.util.HashMap;*/);
        text.append(       " public class " + className + "{");
        text.append(" private "+clz+" obj;");
        text.append(" public " + className + " (){");
        text.append(code);
        text.append(" }");
        text.append(" public "+clz+" getObject(){ return obj;}");
        text.append("@SuppressWarnings(\"unchecked\")\n");
        text.append("public static Object strToObj(String str,Class cls){" +
                "        Gson gson = new Gson();" +
                "        return gson.fromJson(str,cls);" +
                "" +
                "    }");
        //text.append(getClz());
        text.append("}");
        /*text.append("Predicate predicate = new Predicate() {" +
                "            @Override" +
                "            public boolean test(Object o) {" +
                 code+
                "            }" +
                "        };");*/
        String result = text.toString();
        result = result.replaceAll("&quot;","\"");
        //System.out.println(result);
        return result;
    }

    private static String getBaseFileName(File file) {
        String fileName = file.getName();
        String result = "";
        int index = fileName.indexOf(".");

        if (index != -1) {
            result = fileName.substring(0, index);
        } else {
            result = fileName;
        }
        return result;
    }


    public static Predicate getPredicate(String code){
        String str = "this.obj = new Predicate() {"+
                " @Override "+
                " public boolean test(Object o) {"+
                " Map msg = (ConcurrentHashMap)o;String message = (String)msg.get(\"msg\");Map map   =   (HashMap)strToObj(message,HashMap.class);"+
                code +

                "  }}; ";
        //String str = code;
        Predicate object = null;
        try {
            //object = (Predicate) run(str,"getObject","Predicate");
            object = (Predicate) compile2(str,"Predicate","getObject");
            System.out.println(StringUtils.objToJsonStr(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Comparator getComparator(String code){

        String str = "this.obj  = new Comparator() {"+
                " @Override "+
                " public int compare(Object o1, Object o2) {"+
                " Map msg1 = (ConcurrentHashMap)o1;String message1 = (String)msg1.get(\"msg\");Map map1   =   (HashMap)strToObj(message1,HashMap.class);"+
                " Map msg2 = (ConcurrentHashMap)o2;String message2 = (String)msg2.get(\"msg\");Map map2   =   (HashMap)strToObj(message2,HashMap.class);" +
                code +
                /*" MsgMessage msg = (MsgMessage)o;" +
                " return msg.getIsReaded()==1;"+*/
                "  }}; ";
        Comparator object = null;
        try {
            //object = (Comparator) run(str,"getObject","Comparator");
            object = (Comparator) compile2(str,"Comparator","getObject");
            System.out.println(StringUtils.objToJsonStr(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    /**
     * Description: Compile java source file to java class with getTask
     * method, it can specify the class output path and catch diagnostic
     * information
     *
     * @param fullFileName the java source file name with full path
     * @param outputPath   the output path of java class file
     * @return true-compile successfully, false - compile unsuccessfully
     * @throws IOException
     */
    public  boolean compileFile(String fullFileName, String outputPath) throws IOException {
        System.out.println("源文件路径：" + fullFileName);
        System.out.println("输入路径：" + outputPath);
        boolean bRet = false;
        // get compiler
        // TODO 当运行环境是JRE是无法直接ToolProvider.getSystemJavaCompiler();这么获取
        JavaCompiler oJavaCompiler = ToolProvider.getSystemJavaCompiler();

        // define the diagnostic object, which will be used to save the diagnostic information
        DiagnosticCollector<JavaFileObject> oDiagnosticCollector = new DiagnosticCollector<JavaFileObject>();

        // get StandardJavaFileManager object, and set the diagnostic for the object
        StandardJavaFileManager oStandardJavaFileManager = oJavaCompiler
                .getStandardFileManager(oDiagnosticCollector, null, null);

        // set class output location
        JavaFileManager.Location oLocation = StandardLocation.CLASS_OUTPUT;
        try {
            File outputFile = new File(outputPath);
            if (!outputFile.exists()) {
                outputFile.mkdir();
            }
            List<File> dependencies = new ArrayList<File>();

            // 加载依赖的jar文件
            //dependencies.addAll(getJarFiles(System.getProperty("user.dir")+ path));
            // 加载依赖的class文件
            //dependencies.add(new File(System.getProperty("user.dir")
            //        + path+"\\classes\\"));


            oStandardJavaFileManager.setLocation(StandardLocation.CLASS_PATH, dependencies);
            oStandardJavaFileManager.setLocation(oLocation, Arrays
                    .asList(new File[]{outputFile}));

            File sourceFile = new File(fullFileName);
            // get JavaFileObject object, it will specify the java source file.
            Iterable<? extends JavaFileObject> oItJavaFileObject = oStandardJavaFileManager
                    .getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
            // -g 生成所有调试信息
            // -g:none 不生成任何调试信息
            // -g:{lines,vars,source} 只生成某些调试信息
            // -nowarn 不生成任何警告
            // -verbose 输出有关编译器正在执行的操作的消息
            // -deprecation 输出使用已过时的 API 的源位置
            // -classpath <路径> 指定查找用户类文件的位置
            // -cp <路径> 指定查找用户类文件的位置
            // -sourcepath <路径> 指定查找输入源文件的位置
            // -bootclasspath <路径> 覆盖引导类文件的位置
            // -extdirs <目录> 覆盖安装的扩展目录的位置
            // -endorseddirs <目录> 覆盖签名的标准路径的位置
            // -d <目录> 指定存放生成的类文件的位置
            // -encoding <编码> 指定源文件使用的字符编码
            // -source <版本> 提供与指定版本的源兼容性
            // -target <版本> 生成特定 VM 版本的类文件
            // -version 版本信息
            // -help 输出标准选项的提要
            // -X 输出非标准选项的提要
            // -J<标志> 直接将 <标志> 传递给运行时系统

            // 编译选项，将编译产生的类文件放在当前目录下
            //Iterable<String> options = Arrays.asList("-encoding", AutoCodeConstant.CONTENT_ENCODING, "-classpath", getJarFiles(AutoCodeConstant.JARS_PATH));
            Iterable<String> options = Arrays.asList("-encoding", "UTF-8","-classpath",outputPath);
            // compile the java source code by using CompilationTask's call method
            bRet = oJavaCompiler.getTask(null, oStandardJavaFileManager,
                    oDiagnosticCollector, options, null, oItJavaFileObject).call();

            //print the Diagnostic's information
            for (Diagnostic oDiagnostic : oDiagnosticCollector
                    .getDiagnostics()) {
//                log.info("Error on line: "
//                        + oDiagnostic.getLineNumber() + "; URI: "
//                        + oDiagnostic.getSource().toString());
                System.out.printf(
                        "Code: %s%n" +
                                "Kind: %s%n" +
                                "Position: %s%n" +
                                "Start Position: %s%n" +
                                "End Position: %s%n" +
                                "Source: %s%n" +
                                "Message: %s%n",
                        oDiagnostic.getCode(), oDiagnostic.getKind(),
                        oDiagnostic.getPosition(), oDiagnostic.getStartPosition(),
                        oDiagnostic.getEndPosition(), oDiagnostic.getSource(),
                        oDiagnostic.getMessage(null));
            }
        } catch (IOException e) {
            logger.error("动态编译出现异常", e);
        } finally {
            //close file manager
            if (null != oStandardJavaFileManager) {
                try {
                    oStandardJavaFileManager.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("动态编译关闭文件管理器出现异常", e);
                }
            }
        }
        return bRet;
    }

    /**
     * 查找该目录下的所有的jar文件
     *
     * @param jarPath
     * @throws Exception
     */
    private static List<File> getJarFiles(String jarPath) {
        final List<File> dependencyJars = new ArrayList<File>();
        File sourceFile = new File(jarPath);
        if (sourceFile.exists()) {// 文件或者目录必须存在
            if (sourceFile.isDirectory()) {// 若file对象为目录
                // 得到该目录下以.java结尾的文件或者目录
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".jar") ? true : false) {
                                //jars[0] = jars[0] + pathname.getPath() + ";";
                                dependencyJars.add(pathname);
                                return true;
                            }
                            return false;
                        }
                    }
                });
            }
        }
        return dependencyJars;
    }
}
