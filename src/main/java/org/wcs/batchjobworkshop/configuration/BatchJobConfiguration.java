package org.wcs.batchjobworkshop.configuration;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.wcs.batchjobworkshop.entity.HeartBeat;
import org.wcs.batchjobworkshop.repository.HeartBeatRepository;

import java.util.HashMap;
import java.util.Map;

/*O primeiro passo e criar o arquivo de configuraçao do springbatch, temos sempre que instanciar os tres objetos em autowired. */
@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchJobConfiguration {
    /*Nos ajuda a  fazer o job */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    /*Nos ajuda a fazer os steps*/
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private HeartBeatRepository heartBeatRepository;

    /*Criando um objeto que será gerenciado por Spring */
    /* o tasklet posso fazer em uma classe separada com a classe WaitingTasklet ou posso criar uma classe anonima que vai implementar o metodo execute */
    @Bean
    public Step heartBeat() {
        return this.stepBuilderFactory
                .get("heartBeatStep")
                .tasklet(
                        new Tasklet() {
                            @Override
                            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                                heartBeatRepository.save(new HeartBeat());
                                /* verifica se o step é realmente terminado para continuar o step*/
                                return RepeatStatus.FINISHED;
                            }
                            /*o allowStart permite lançar o step cada vez que o servidor é inicializado, senao só vai ser executado 1x */
                        }).allowStartIfComplete(true).build();
    }

    @Bean
      public Job heartBeatJob() {
        /*Aqui eu crio o job que leva um nome e após eu indico o step que preciso */

        return this.jobBuilderFactory.get("heartBeatJob").start(this.heartBeat()).build();
    }

    /*Posso aplicar aqui o ThreadPoolTaskExecutor para criar um comportamento asynchrone */

    /* Aqui eu lanço o job a cada minuto, utilizando o cron */
    @Scheduled(cron = "* * * * * ?")
    public void callHeartBeat() {

        Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(this.heartBeatJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        }

    }
}
