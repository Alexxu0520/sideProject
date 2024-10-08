package org.example;

import com.fasterxml.jackson.annotation.JsonAlias;

import jdk.jfr.Category;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
public class AuditService {
    private static final Gson gson = new Gson();
    public AuditResultRsp audit(AuditInputReq request) {
        // 获取内容审核结果
        ModerationDto moderationDto = getModeration(request);

        // 创建审核请求对象
        TdAuditRequest auditRequest = new TdAuditRequest();
        auditRequest.setUserId(request.getUserId());
        auditRequest.setInput(request.getInput());

        // 创建审核响应对象
        TdAuditResponse auditResponse = new TdAuditResponse();
        auditResponse.setUserId(request.getUserId());
        auditResponse.setRequestId(auditRequest.getId());

        // 创建审核结果对象
        TdAuditResult auditResult = new TdAuditResult();
        auditResult.setInput(request.getInput());
        auditResult.setRequestId(auditRequest.getId());

        // 处理审核标签
        List<AuditTagEnum> tags = this.setTags(
                moderationDto.getResults().get(0).getCategories(),
                auditResponse
        );

        // 处理审核分数
        this.setScores(
                moderationDto.getResults().get(0).getCategoryScores(),
                auditResponse
        );

        // 设置审核结果标签字符串
        auditResult.setTags(this.getTagsString(tags));

        // 保存审核请求
        this.save(auditRequest);

        // 保存原始审核响应
        tdAuditResponseService.save(auditResponse);

        // 保存处理后的审核结果
        tdAuditResultService.save(auditResult);

        // 拼接返回参数
        AuditResultRsp auditResultRsp = new AuditResultRsp();
        auditResultRsp.setInput(request.getInput());
        auditResultRsp.setTags(tags);

        // 返回结果响应
        return auditResultRsp;
    }
    private ModerationDto getModeration(AuditInputReq request) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1200, TimeUnit.SECONDS)
                .writeTimeout(1200, TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS)
                .build();

        MediaType type = MediaType.parse("application/json; charset=utf-8");
        String jsonRequest = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(jsonRequest, type);
        final Request okRequest = new Request.Builder()
                .url("https://api.openai.com/v1/moderations/")
                // not finish
                .build();
        try {
            Response response = client.newCall(okRequest).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return gson.fromJson(responseBody, ModerationDto.class);
            }
            else {
                throw new IOException("Unexpected code" + response);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<AuditTagEnum> setTags(Categorie categorie, TdAuditResponse auditResponse) {
        List<AuditTagEnum> tags = new ArrayList<>();

        if (categorie.getHarassment()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getHarassment().getClass().getName()));
            auditResponse.setHarassment(1);
        }

        if (categorie.getSelfHarm()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getSelfHarm().getClass().getName()));
            auditResponse.setSelfharm(1);
        }

        if (categorie.getSexualMinors()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getSexualMinors().getClass().getName()));
            auditResponse.setSexualMinors(1);
        }

        if (categorie.getHateThreatening()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getHateThreatening().getClass().getName()));
            auditResponse.setHarassmentThreatening(1);
        }

        if (categorie.getViolenceGraphic()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getViolenceGraphic().getClass().getName()));
            auditResponse.setViolenceGraphic(1);
        }

        if (categorie.getSelfHarmIntent()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getSelfHarmIntent().getClass().getName()));
            auditResponse.setSelfharmIntent(1);
        }

        if (categorie.getSelfHarmInstructions()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getSelfHarmInstructions().getClass().getName()));
            auditResponse.setSelfharmInstructions(1);
        }

        if (categorie.getHarassmentThreatening()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getHarassmentThreatening().getClass().getName()));
            auditResponse.setHarassmentThreatening(1);
        }

        if (categorie.getViolence()) {
            tags.add(AuditTagEnum.parseOfNullable(categorie.getViolence().getClass().getName()));
            auditResponse.setViolence(1);
        }

        return tags;
    }

    public List<AuditResultRsp> getAuditResult(Long userId) {
        List<TdAuditResult> auditResultList = tdAuditResultService.lambdaQuery()
                .eq(TdAuditResult::getUserId, userId)
                .list();

        List<AuditResultRsp> auditResultRspList = new ArrayList<>();

        for (TdAuditResult auditResult : auditResultList) {
            AuditResultRsp auditResultRsp = new AuditResultRsp();

            auditResultRsp.setTags(this.getTagsEnum(auditResult.getTags()));

            auditResultRsp.setInput(auditResult.getInput());

            auditResultRspList.add(auditResultRsp);
        }

        return auditResultRspList;
    }

    private List<AuditTagEnum> getTagsEnum(String tagsStr) {
        List<String> tagList = Arrays.asList(tagsStr.split(","));

        List<AuditTagEnum> tagEnums = new ArrayList<>();

        for (String tag : tagList) {
            tagEnums.add(AuditTagEnum.parseOfNullable(tag));
        }

        return tagEnums;
    }
    private void setScores(CategoryScore categoryScore, TdAuditResponse auditResponse) {
        auditResponse.setSexualScore(categoryScore.getSexual());
        auditResponse.setHateScore(categoryScore.getHate());
        auditResponse.setHarassmentScore(categoryScore.getHarassment());
        auditResponse.setSelfharmScore(categoryScore.getSelfHarm());
        auditResponse.setSexualMinorsScore(categoryScore.getSexualMinors());
        auditResponse.setHateThreateningScore(categoryScore.getHateThreatening());
        auditResponse.setViolenceGraphicScore(categoryScore.getViolenceGraphic());
        auditResponse.setSelfharmIntentScore(categoryScore.getSelfHarmIntent());
        auditResponse.setSelfharmInstructionsScore(categoryScore.getSelfHarmInstructions());
        auditResponse.setHarassmentThreateningScore(categoryScore.getHarassmentThreatening());
        auditResponse.setViolenceScore(categoryScore.getViolence());
    }
    // 通过枚举获取标签内容，并用逗号隔开
    private String getTagsString(List<AuditTagEnum> tags) {
        // 初始化结果字符串为 null
        String tagStr = null;

        // 遍历标签列表
        for (AuditTagEnum auditTagEnum : tags) {
            // 如果结果字符串不为空，说明已有标签，需要加逗号
            if (tagStr != null) {
                tagStr += "," + auditTagEnum.getTag();
            } else {
                // 如果结果字符串为空，直接设置为当前标签的值
                tagStr = auditTagEnum.getTag();
            }
        }

        // 返回由逗号隔开的标签字符串
        return tagStr;
    }
}
