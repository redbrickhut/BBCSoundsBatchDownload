package org.menzies.model.library;

import org.apache.commons.csv.CSVRecord;
import org.menzies.model.pojo.Tag;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.menzies.utils.StringUtils.makeFileSafe;

public class BBCConfig implements LibraryConfig {

    public static final File sourceCSV;

    static {
        sourceCSV = new File(BBCConfig.class.getResource("/csv/BBC.csv").getFile());
        System.out.println("past source init");
    }


    @Override
    public File getCSV() {
        return sourceCSV;
    }

    @Override
    public String getDefaultSubDir(CSVRecord record) {

        String cdName = makeFileSafe(record.get("CD_NAME"));
        String cdNumber = makeFileSafe(record.get("CD_NUMBER"));
        String fileName = makeFileSafe(record.get("DESCRIPTION"));
        String category = makeFileSafe(record.get("CATEGORY"));


        StringBuilder builder = new StringBuilder();

        builder.append('\\');

        if (category.length() > 0 && category.length() < 64) {
            builder.append(category);
            builder.append('\\');
        }


        if (cdName.length() > 0) {
            builder.append(cdName);
        } else builder.append("Unknown Album");


        builder.append(String.format("\\%s", fileName));

        return builder.toString();
    }

    @Override
    public String getSource(CSVRecord record) {
        return "http://bbcsfx.acropolis.org.uk/assets/" + record.get(0) ;
    }

    @Override
    public Set<Tag> getDefaultTags(CSVRecord record) {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("ARTIST", "BBC"));
        tags.add(new Tag("GENRE", "Sound Effects"));
        tags.add(new Tag("ALBUM", "BBC: " + record.get("CD_NAME")));
        tags.add(new Tag("TITLE", record.get("DESCRIPTION")));
        return tags;
    }

    @Override
    public String[] getHeaders() {
        return new String[] {"LOCATION", "DESCRIPTION", "SECONDS", "CATEGORY",
                           "CD_NUMBER", "CD_NAME", "TRACK_NO"};
    }

    @Override
    public String getDescription() {
        return "These 16,000 BBC Sound Effects are made available by the BBC in WAV format to download " +
                "for use under the terms of the RemArc Licence. The Sound Effects are BBC copyright," +
                " but they may be used for personal, educational or research purposes, as detailed " +
                "in the license.\n\n" +
                "Approximate size of download is 285 GB.";
    }

    @Override
    public String getFileExt() {
        return ".wav";
    }
}
