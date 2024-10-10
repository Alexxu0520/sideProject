package org.example;

import com.fasterxml.jackson.annotation.JsonAlias;

import jdk.jfr.Category;
import okhttp3.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Connection;

@Service
public class AuditService {

    @Autowired
    private TdAuditResponseService tdAuditResponseService;

    @Autowired
    private TdAuditResultService tdAuditResultService;

    @Autowired
    private TdAuditRequestMapper tdAuditRequestMapper;

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
                (Categorie) moderationDto.getResults().get(0).getCategories(),
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

    public void save(TdAuditRequest auditRequest) {
        String insertSQL = "INSERT INTO td_audit_request (user_id, input, create_time) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = ((java.sql.Connection) connection).prepareStatement(insertSQL)) {

            preparedStatement.setLong(1, auditRequest.getUserId());
            preparedStatement.setString(2, auditRequest.getInput());
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(auditRequest.getCreateTime()));

            preparedStatement.executeUpdate();
            System.out.println("AuditRequest saved to PostgreSQL");

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        if (categorie.isHarassment()) {
            tags.add(AuditTagEnum.parseOfNullable("Harassment"));
            auditResponse.setHarassment(1);
        }

        if (categorie.isSelfHarm()) {
            tags.add(AuditTagEnum.parseOfNullable("SelfHarm"));
            auditResponse.setSelfHarm(1);
        }

        if (categorie.isSexualMinors()) {
            tags.add(AuditTagEnum.parseOfNullable("SexualMinors"));
            auditResponse.setSexualMinors(1);
        }

        if (categorie.isHateThreatening()) {
            tags.add(AuditTagEnum.parseOfNullable("HateThreatening"));
            auditResponse.setHarassmentThreatening(1);
        }

        if (categorie.isViolenceGraphic()) {
            tags.add(AuditTagEnum.parseOfNullable("ViolenceGraphic"));
            auditResponse.setViolenceGraphic(1);
        }

        if (categorie.isSelfHarmIntent()) {
            tags.add(AuditTagEnum.parseOfNullable("SelfHarmIntent"));
            auditResponse.setSelfHarmIntent(1);
        }

        if (categorie.isSelfHarmInstructions()) {
            tags.add(AuditTagEnum.parseOfNullable("SelfHarmInstructions"));
            auditResponse.setSelfHarmInstructions(1);
        }

        if (categorie.isHarassmentThreatening()) {
            tags.add(AuditTagEnum.parseOfNullable("HarassmentThreatening"));
            auditResponse.setHarassmentThreatening(1);
        }

        if (categorie.isViolence()) {
            tags.add(AuditTagEnum.parseOfNullable("Violence"));
            auditResponse.setViolence(1);
        }

        return tags;
    }



    public List<AuditResultRsp> getAuditResult(Long userId) {
        // Fetch results using the service
        List<TdAuditResult> auditResultList = tdAuditResultService.getAuditResultsByUserId(userId);

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
        auditResponse.setSelfHarmScore(categoryScore.getSelfHarm());
        auditResponse.setSexualMinorsScore(categoryScore.getSexualMinors());
        auditResponse.setHateThreateningScore(categoryScore.getHateThreatening());
        auditResponse.setViolenceGraphicScore(categoryScore.getViolenceGraphic());
        auditResponse.setSelfHarmIntentScore(categoryScore.getSelfHarmIntent());
        auditResponse.setSelfHarmInstructionsScore(categoryScore.getSelfHarmInstructions());
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
