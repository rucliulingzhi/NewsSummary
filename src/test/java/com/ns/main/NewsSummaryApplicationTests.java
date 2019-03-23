package com.ns.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ns.main.util.NLPUtil;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsSummaryApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		/*String modelString = "models/chineseFactored.ser";
		LexicalizedParser lp = LexicalizedParser.loadModel(modelString);
		String sentence = "被巴基斯坦俘获的印度飞行员阿比纳丹·瓦尔塔曼走向印度和巴基斯坦边境瓦加口岸印方一侧。";
		Tree tree = lp.apply(MainPartExtractor.seg(sentence));
		System.out.println(tree.getNodeNumber(3));;
		for(int i = 0; i < tree.children()[0].children().length; i++) {
			System.out.println(tree.children()[0].children()[i]);
		}*/
	    //NLPUtil.getPerson("被巴基斯坦俘获的印度飞行员阿比纳丹·瓦尔塔曼走向印度和巴基斯坦边境瓦加口岸印方一侧。", pipeline);
		Properties props = new Properties();
	    props.load(this.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties"));
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    List<String> sentList = new ArrayList<String>();
	    
	    NLPUtil.getPerson(sentList, pipeline)
	    .forEach(System.out::println);;
	}

}
