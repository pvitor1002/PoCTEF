package br.com.puc.TEF.adapters.gateway.filters;

import br.com.puc.TEF.adapters.gateway.channel.GatewayChannels;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.messaging.Message;

@Configuration
@RequiredArgsConstructor
public class IntegrationFlowDefinitions {

    public static final String HANDLER_FLOW = "handlerFlow";

    public static final String instanceId = "8085";

    private final MessageFilter messageFilter;

    @Bean
    public IntegrationFlow requestsFlow() {
        return IntegrationFlows.from(HANDLER_FLOW).enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.header("instanceId",instanceId))
                .transform(new ObjectToJsonTransformer())
                .channel(GatewayChannels.REQUEST).get();
    }

    @Bean
    public IntegrationFlow replyFiltererFlow() {
        return IntegrationFlows.from(GatewayChannels.REPLY)
                .route(Message.class, messageFilter::accept, r -> r
                        .channelMapping(false, GatewayChannels.LATERAL)
                        .channelMapping(true, GatewayChannels.RESPONSE)
                )
                .get();
    }

    @Bean
    public IntegrationFlow restFiltererFlow() {
        return IntegrationFlows.from(GatewayChannels.LATERAL)
                .handle(Http.outboundGateway("http://localhost:8082/response")
                        .httpMethod(HttpMethod.POST)
                        .mappedRequestHeaders("replyChannel")
                        .headerMapper(headerMapper())
                        .extractPayload(true))
                .transform(new ObjectToJsonTransformer())
                .channel("nullChannel")
                .get();
    }

    @Bean
    HeaderMapper headerMapper() {
        final DefaultHttpHeaderMapper headerMapper = new DefaultHttpHeaderMapper();
        final String[] headerNames = { "replyChannel" };
        headerMapper.setOutboundHeaderNames(headerNames);

        headerMapper.setUserDefinedHeaderPrefix("");
        return headerMapper;
    }
}