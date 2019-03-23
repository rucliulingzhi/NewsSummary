package com.ns.main.util;

import java.io.IOException;
import java.util.Properties;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ns.main.config.Paths;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class ModelUtil {

    private static Logger log = LoggerFactory.getLogger(ModelUtil.class);

    public static void trainModel(String fileName) throws Exception {
    	String filePath = Paths.NLPROOT.getValue() + fileName;
        String modelPath = filePath + ".model";
        log.info("Load & Vectorize Sentences....");
        SentenceIterator iter = new BasicLineIterator(filePath);
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();
        log.info("Fitting Word2Vec model....");
        vec.fit();
        log.info("Writing word vectors to text file....");
        log.info("Closest Words:");
        WordVectorSerializer.writeWord2VecModel(vec, modelPath);
    }
    
    public static Word2Vec readModel(String fileName) {
    	fileName = "news.zh.txt";
    	Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel(Paths.NLPROOT.getValue() + fileName + ".model");
    	return word2Vec;
    }
    
    public static StanfordCoreNLP getPropNlp() {
    	Properties props = new Properties();
		try {
			props.load(GetClassLoader.class.getResourceAsStream("/StanfordCoreNLP-chinese.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		return pipeline;
    }
    
}