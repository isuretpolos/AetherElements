package isuret.polos.aether.domains;

import org.dizitart.no2.objects.Id;

import java.util.UUID;

/**
 * A paragraph is a series of related sentences developing a central idea, called the topic.
 * Try to think about paragraphs in terms of thematic unity: a paragraph is a sentence or a group of sentences that
 * supports one central, unified idea. Paragraphs add one idea at a time to your broader argument.
 *
 * In this case the paragraph is a wrapper around different types of objects
 */
public class Paragraph {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String header;
    private Note note;
    private AnalysisResult analysisResult;
    private BroadCastData broadCastData;

    public Paragraph(){}
    public Paragraph(Note note) { this.note = note; }
    public Paragraph(AnalysisResult analysisResult) {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public BroadCastData getBroadCastData() {
        return broadCastData;
    }

    public void setBroadCastData(BroadCastData broadCastData) {
        this.broadCastData = broadCastData;
    }
}
