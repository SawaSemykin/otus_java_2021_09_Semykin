package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.NumbersRangeMessage;
import ru.otus.protobuf.generated.RemoteNumberGeneratorGrpc;

public class RemoteNumberGeneratorImpl extends RemoteNumberGeneratorGrpc.RemoteNumberGeneratorImplBase {

    private final RealNumberGenerator realNumberGenerator;

    public RemoteNumberGeneratorImpl(RealNumberGenerator realNumberGenerator) {
        this.realNumberGenerator = realNumberGenerator;
    }

    @Override
    public void startGenerating(NumbersRangeMessage request, StreamObserver<NumberMessage> responseObserver) {
        for (long i = request.getFirstValue(); i <= request.getSecondValue(); i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(toNumberMessage(i));
        }
        responseObserver.onCompleted();
    }

    private NumbersRangeMessage toNumbersRangeMessage(long first, long second) {
        return NumbersRangeMessage.newBuilder()
                .setFirstValue(first)
                .setSecondValue(second)
                .build();
    }

    private NumberMessage toNumberMessage(long value) {
        return NumberMessage.newBuilder()
                .setValue(value)
                .build();
    }
}
