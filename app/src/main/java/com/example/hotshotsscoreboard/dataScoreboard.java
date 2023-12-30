package com.example.hotshotsscoreboard;

public class dataScoreboard {

    private String courtname;
    private int gamenum;
    private int score1;
    private int score2;
    private String team1;
    private String team2;

    public dataScoreboard(String courtname, int gamenum, int score1, int score2, String team1, String team2)
    {
        this.courtname = courtname;
        this.gamenum = gamenum;
        this.score1 = score1;
        this.score2 = score2;
        this.team1 = team1;
        this.team2 = team2;
    }

    public String getCourtname() {
        return courtname;
    }

    public void setCourtname(String courtname) {
        this.courtname = courtname;
    }

    public int getGamenum() {
        return gamenum;
    }

    public void setGamenum(int gamenum) {
        this.gamenum = gamenum;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }
}
