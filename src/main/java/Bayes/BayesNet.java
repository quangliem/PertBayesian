package Bayes;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bayesserver.State;
import com.bayesserver.Table;
import com.bayesserver.inference.InconsistentEvidenceException;
import com.bayesserver.inference.Inference;
import com.bayesserver.inference.InferenceFactory;
import com.bayesserver.inference.QueryOptions;
import com.bayesserver.inference.QueryOutput;
import com.bayesserver.inference.RelevanceTreeInferenceFactory;

public class BayesNet {
	private RisksNet risknet;

	public BayesNet() throws IOException, InconsistentEvidenceException {
		for (int id = 1; id <= 7; id++) {
			risknet = new RisksNet(Integer.toString(id));
			InferenceFactory factory = new RelevanceTreeInferenceFactory();
			Inference inference = factory.createInferenceEngine(risknet.getRiskNet());
			QueryOptions queryOptions = factory.createQueryOptions();
			QueryOutput queryOutput = factory.createQueryOutput();
			System.out.println("D:/distribution/probability" + id +".bin");
			// Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
			FileOutputStream fos = new FileOutputStream("D:/distribution/probability" + id +".bin");
			DataOutputStream dos = new DataOutputStream(fos);

			// Bước 2: Ghi dữ liệu

			for (int i = 0; i < 23; i++) {
				Table queryRisk = new Table(risknet.getRiskNet().getNodes().get(i));
				State stateTrue = risknet.getRiskNet().getNodes().get(i).getVariables().get(0).getStates().get("True");
				State stateFalse = risknet.getRiskNet().getNodes().get(i).getVariables().get(0).getStates()
						.get("False");
				inference.getQueryDistributions().add(queryRisk);
				inference.query(queryOptions, queryOutput);
				System.out.println(
						"P(" + (i + 1) + ") = {" + queryRisk.get(stateTrue) + ", " + queryRisk.get(stateFalse) + "}.");

				dos.writeDouble(queryRisk.get(stateTrue));
			}

			// Bước 3: Đóng luồng
			fos.close();
			dos.close();
			System.out.println("Done!");
		}
	}

	public static void main(String[] args) throws IOException, InconsistentEvidenceException {
		BayesNet bayesNet = new BayesNet();
	}
}
