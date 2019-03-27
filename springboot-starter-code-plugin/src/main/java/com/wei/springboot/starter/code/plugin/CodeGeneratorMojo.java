package com.wei.springboot.starter.code.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author William
 * @Date 2019/3/22
 * @Description 代码生成插件
 */
@Mojo(name = "generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.TEST)
public class CodeGeneratorMojo extends AbstractMojo {


    @Parameter(defaultValue = "${project.basedir}/src/main/resources/generatorConfig.xml", required = true)
    private File configurationFile;
    @Parameter(defaultValue = "true")
    private boolean overwrite;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            List<String> warnings = new ArrayList<>();
            Configuration config = new Configuration();
            if (configurationFile.exists()) {
                ConfigurationParser cp = new ConfigurationParser(warnings);
                config = cp.parseConfiguration(configurationFile);
            }else {
                // 构建配置
                // 该模型为每一张表只生成一个实体类。这个实体类包含表中的所有字段。
                config.addContext(new Context(ModelType.FLAT));
//                config.getContext()
            }
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            for (String warning : warnings) {
                System.out.println(warning);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
