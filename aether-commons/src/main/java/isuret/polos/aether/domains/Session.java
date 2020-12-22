package isuret.polos.aether.domains;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The session is a post in a blog
 */
public class Session {

    private Calendar created = Calendar.getInstance();
    private String intention;
    private String description;
    private List<AnalysisResult> analysisResults = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private List<BroadCastData> broadCastDataList = new ArrayList<>();

    public List<BroadCastData> getBroadCastDataList() {
        return broadCastDataList;
    }

    public void setBroadCastDataList(List<BroadCastData> broadCastDataList) {
        this.broadCastDataList = broadCastDataList;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public List<AnalysisResult> getAnalysisResults() {
        return analysisResults;
    }

    public void setAnalysisResults(List<AnalysisResult> analysisResults) {
        this.analysisResults = analysisResults;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
