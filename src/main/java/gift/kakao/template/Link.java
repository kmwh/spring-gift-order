package gift.kakao.template;

public record Link(
    String web_url,
    String mobile_web_url,
    String android_execution_params,
    String ios_execution_params
) {}
