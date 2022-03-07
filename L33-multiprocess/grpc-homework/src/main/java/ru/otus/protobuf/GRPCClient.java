package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.NumbersRangeMessage;
import ru.otus.protobuf.generated.RemoteNumberGeneratorGrpc;
import ru.otus.protobuf.service.NumberPrinter;

public class GRPCClient {

    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var printer = new NumberPrinter();
        var stub = RemoteNumberGeneratorGrpc.newStub(channel);
        stub.startGenerating(
                NumbersRangeMessage.newBuilder()
                        .setFirstValue(1)
                        .setSecondValue(30)
                        .build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(NumberMessage value) {
                        log.info("Server has generated new value: {}", value.getValue());
                        printer.onNext(value.getValue());
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("", t);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("Server stopped generating numbers");
                    }
                });

        printer.startPrinting();

        channel.shutdown();
    }
}
