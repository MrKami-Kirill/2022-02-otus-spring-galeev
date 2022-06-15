package ru.otus.spring.config.component;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ListDelegateWriter<T> implements ItemWriter<List<T>>, ItemStream {

    private ItemWriter<T> delegate;

    public ListDelegateWriter(ItemWriter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(List<? extends List<T>> items) throws Exception {
        for (List<T> item : items) {
            delegate.write(item);
        }
    }

    public void setDelegate(ItemWriter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream) delegate).open(executionContext);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream) delegate).update(executionContext);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream) delegate).close();
        }
    }
}
