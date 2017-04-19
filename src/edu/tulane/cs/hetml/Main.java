package edu.tulane.cs.hetml;

import edu.tulane.cs.hetml.mSpRL.Eval.*;
import edu.tulane.cs.hetml.mSpRL.SpRL2013.LANDMARK;
import edu.tulane.cs.hetml.mSpRL.SpRL2013.RELATION;
import edu.tulane.cs.hetml.mSpRL.SpRL2013.SPATIALINDICATOR;
import edu.tulane.cs.hetml.mSpRL.SpRL2013.TRAJECTOR;
import edu.tulane.cs.hetml.mSpRL.SpRL2017.Scene;
import edu.tulane.cs.hetml.mSpRL.SpRL2017.Sentence;
import edu.tulane.cs.hetml.mSpRL.SpRL2017.SpRL2017Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {

    private static String actualFile;
    private static String predictedFile;
    private static File outputFile;
    private static String matchingType;
    private static SpRL2017Document actualDoc;
    private static SpRL2017Document predictedDoc;
    private static EvalComparer comparer;
    private static FileOutputStream outputStream;

    public static void main(String[] args) throws IOException {
        readArgs(args);
        XmlSpRLEvaluator xmlEvaluator = new XmlSpRLEvaluator(actualFile, predictedFile, comparer);
        SpRLEvaluator evaluator = new SpRLEvaluator();

        List<SpRLEvaluation> roleResults = xmlEvaluator.evaluateRoles();
        printResults("Role results", roleResults);

        List<SpRLEvaluation> relationResults = xmlEvaluator.evaluateRelations();
        printResults("Relation results", relationResults);

        SpRLEvaluation relEval = relationResults.get(0);

        List<SpRLEvaluation> generalTypeResults = evaluator.evaluateRelationGeneralType(relEval);
        printResults("General Type results", generalTypeResults);

        List<SpRLEvaluation> specificTypeResults = evaluator.evaluateRelationSpecificType(relEval);
        printResults("Specific Type results", specificTypeResults);

        List<SpRLEvaluation> rcc8Results = evaluator.evaluateRelationRCC8(relEval);
        printResults("RCC8 results", rcc8Results);

        List<SpRLEvaluation> forResults = evaluator.evaluateRelationFoR(relEval);
        printResults("FoR results", forResults);

        outputStream.close();
    }

    private static void printResults(String caption, List<SpRLEvaluation> evals) {
        SpRLEvaluator.printEvaluation(caption, evals);
        SpRLEvaluator.printEvaluation(caption, outputStream, evals);
    }

    private static void combine(String file1, String file2, String resultFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SpRL2017Document.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Marshaller marshaller = jaxbContext.createMarshaller();

            actualDoc = (SpRL2017Document) jaxbUnmarshaller.unmarshal(new File(file1));
            predictedDoc = (SpRL2017Document) jaxbUnmarshaller.unmarshal(new File(file2));
            Scene lastScene = actualDoc.getScenes().get(actualDoc.getScenes().size() - 1);
            Sentence lastSentence = lastScene.getSentences().get(lastScene.getSentences().size() - 1);
            int offset = lastSentence.getEnd() + 1;
            for (Scene s : predictedDoc.getScenes())
                for (Sentence sentence : s.getSentences()) {
                    sentence.setStart(offset + sentence.getStart());
                    sentence.setEnd(offset + sentence.getEnd());
                    for (LANDMARK l : sentence.getLandmarks())
                        l.setId("2_" + l.getId());
                    for (SPATIALINDICATOR sp : sentence.getSpatialindicators())
                        sp.setId("2_" + sp.getId());
                    for (TRAJECTOR t : sentence.getTrajectors())
                        t.setId("2_" + t.getId());
                    for (RELATION r : sentence.getRelations()) {
                        r.setTrajectorId("2_" + r.getTrajectorId());
                        r.setLandmarkId("2_" + r.getLandmarkId());
                        r.setSpatialIndicatorId("2_" + r.getSpatialIndicatorId());
                        r.setId("2_" + r.getId());
                    }
                }
            SpRL2017Document all = new SpRL2017Document();
            all.setScenes(actualDoc.getScenes());
            all.getScenes().addAll(predictedDoc.getScenes());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(all, new File(resultFile));

        } catch (JAXBException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void readArgs(String[] args) {
        if (args.length != 4) {
            error("Usage: mSpRLEval actual.xml predicted.xml output.txt matching(o:overlap/e:exact)");
        }

        actualFile = args[0];
        if (!new File(actualFile).exists())
            error("actual file doesn't exist.");

        predictedFile = args[1];
        if (!new File(predictedFile).exists())
            error("predictions file doesn't exist.");

        outputFile = new File(args[2]);
        outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        matchingType = args[3].toLowerCase();
        if (matchingType.equals("o") && matchingType.equals("e"))
            error("invalid matching type.");
        if (matchingType.equals("o"))
            comparer = new OverlapComparer();
        else
            comparer = new ExactComparer();
    }

    private static void error(String message) {
        System.out.println(message);
        System.exit(1);
    }
}
