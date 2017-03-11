package cqupt.myinvest.bean;

/**
 * 从服务器获取的理财产品
 */
public class Product {
    public String id;
    public String memberNum;
    public String minTouMoney;
    public String money;
    public String name;
    public String progress;
    public String suodingDays;
    public String yearRate;

    public void setId(String id) {
        this.id = id;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public void setMinTouMoney(String minTouMoney) {
        this.minTouMoney = minTouMoney;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setSuodingDays(String suodingDays) {
        this.suodingDays = suodingDays;
    }

    public void setYearRate(String yearRate) {
        this.yearRate = yearRate;
    }

    public String getId() {
        return id;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public String getMinTouMoney() {
        return minTouMoney;
    }

    public String getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public String getProgress() {
        return progress;
    }

    public String getSuodingDays() {
        return suodingDays;
    }

    public String getYearRate() {
        return yearRate;
    }
}
