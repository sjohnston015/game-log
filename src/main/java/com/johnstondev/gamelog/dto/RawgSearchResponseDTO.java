package com.johnstondev.gamelog.dto;

import java.util.List;

public class RawgSearchResponseDTO {
    private Integer count;
    private String next;
    private String previous;
    private List<GameSearchResultDTO> results;

    public RawgSearchResponseDTO() {}

    // getters and setters !!!!
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }

    public String getPrevious() { return previous; }
    public void setPrevious(String previous) { this.previous = previous; }

    public List<GameSearchResultDTO> getResults() { return results; }
    public void setResults(List<GameSearchResultDTO> results) { this.results = results; }
}
