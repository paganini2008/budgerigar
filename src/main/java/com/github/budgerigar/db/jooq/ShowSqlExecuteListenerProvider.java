package com.github.budgerigar.db.jooq;

import org.jetbrains.annotations.NotNull;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: ShowSqlExecuteListenerProvider
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Component
public class ShowSqlExecuteListenerProvider implements ExecuteListenerProvider {

    @Override
    public @NotNull ExecuteListener provide() {
        return new ShowSqlExecuteListener();
    }

    @Slf4j
    private static class ShowSqlExecuteListener implements ExecuteListener {

        private static final long serialVersionUID = 1L;
        private long startTime;

        @Override
        public void start(ExecuteContext ctx) {
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            long elapsed = System.currentTimeMillis() - startTime;
            log.info("Executed sql: {}", ctx.sql());
            log.info("Binded values: {}", ctx.statement());
            log.info("Take time (ms): {}", elapsed);
        }

        @Override
        public void exception(ExecuteContext ctx) {
            Throwable e = ctx.exception();
            if (e != null) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        @Override
        public void warning(ExecuteContext ctx) {
            if (ctx.sqlWarning() != null) {
                if (log.isWarnEnabled()) {
                    log.warn("SQL Warning: {}", ctx.sqlWarning().getMessage());
                }
            }
        }



    }

}
