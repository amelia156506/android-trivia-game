/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * This class is for dashboard logging, a request from Android will create one log
 */
package ds;

public class Log {
    private String _id, ResponseCode, Category, Difficulty, Question, CorrectAns, WrongAns1, WrongAns2, WrongAns3, ReceivingTime, ResponseTime;
    private long Latency;

    public String get_id() {
        return _id;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public String getCategory() {
        return Category;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public String getQuestion() {
        return Question;
    }

    public String getCorrectAns() {
        return CorrectAns;
    }

    public String getWrongAns1() {
        return WrongAns1;
    }

    public String getWrongAns2() {
        return WrongAns2;
    }

    public String getWrongAns3() {
        return WrongAns3;
    }

    public String getReceivingTime() {
        return ReceivingTime;
    }

    public String getResponseTime() {
        return ResponseTime;
    }

    public long getLatency() {
        return Latency;
    }
}
