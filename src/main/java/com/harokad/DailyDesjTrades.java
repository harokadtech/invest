package com.harokad;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.hofman.projectsGmailApi.Project;

public class DailyDesjTrades {

    public static void main(String[] args) throws ParseException {
        parseCurrentProjects();
    }

    public static List<Project> parseCurrentProjects() {
        StringBuilder strInput = new StringBuilder();
        strInput.append(INPUT_DATAX);
        strInput.append(INPUT_DATAX_B);
        strInput.append(INPUT_DATAX_A);
        return parseProjects(strInput.toString());
    }

    public static List<Project> parseProjects(String string) {
        List<Project> allProjects = new ArrayList<Project>();

        String[] orderLines = string.split("\\n");
        try {
            for (String order : orderLines) {
                String[] parts = order.split("\\t");
                if (parts.length < 7) {
                    continue;
                }
                String status = parts[2];
                if (!status.equals("Exécuté")) {
                    continue;
                }
                String type = parts[3];
                String qty = parts[4];
                if (type.equals("Achat")) {
                    type = "Buy";
                } else if (type.equals("Vente")) {
                    type = "Vente";
                } else {
                    continue;
                }
                String name = parts[5];
                name = name.replaceAll("-C", "").trim();
                String price = parts[7];
                price = price.replaceAll(",", ".");
                String from = parts[1];
                String accType = GmailInvestExtract.ACCOUNT_TYPES.get(from);
                from = "DESJ";
                String dateStr = parts[9];
                if (!dateStr.contains("2023")) {
                    dateStr = parts[10];
                }
                Date date = Project.S_DATE_FORMAT.parse(dateStr);
                String transTime = Project.formatTimeMillis(date.getTime() + 3600 * 1000 * 12);
                String messageBody = MessageFormat.format(ReadDesjHistExcel.TEMPLATE, from, type, name, qty,
                        price.toString(), transTime, accType);
                Project project = new Project(messageBody);
                project.parseData();
                allProjects.add(project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allProjects;
        // GmailInvestExtract.extracted(GmailInvestExtract.FROM_TIME, true, false,
        // allProjects);
    }



     private static String INPUT_DATAX_B = """ 
        000009048000	5KVXEZ7	Exécuté	Vente	25	LEV   	0,00	2,55	2,55	0,00	31 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	100	FOOD  	0,00	0,48	0,48	0,00	31 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	9	HR.UN 	0,00	10,15	10,15	0,00	30 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	9	HR.UN 	0,00	10,17	10,17	0,00	30 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	1	SHOP  	0,00	80,35	80,35	0,00	30 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	100	FOOD  	0,00	0,48	0,48	0,00	29 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	12	LSPD  	0,00	17,18	17,18	0,00	26 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	12	LSPD  	0,00	17,50	17,50	0,00	26 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	5	NVEI  	0,00	43,30	43,30	0,00	26 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	6	SHOP  	0,00	80,50	80,50	0,00	26 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	5	SHOP  	0,00	81,39	81,39	0,00	26 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	7	LEV   	0,00	2,70	2,70	0,00	23 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	5	SHOP  	0,00	81,64	81,64	0,00	23 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	12	LSPD  	0,00	19,02	19,02	0,00	23 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	8	NVEI  	0,00	45,20	45,20	0,00	23 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	2	SHOP  	0,00	81,14	81,14	0,00	23 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	52	LSPD  	0,00	17,60	17,60	0,00	18 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	13	NVEI  	0,00	44,10	44,10	0,00	18 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	35	LSPD  	0,00	20,13	20,13	0,00	17 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	5	NVEI  	0,00	44,95	44,95	0,00	17 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	10	LSPD  	0,00	20,26	20,26	0,00	17 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	27	LSPD  	0,00	19,68	19,68	0,00	17 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	5	NVEI  	0,00	45,13	45,13	0,00	17 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	10	NVEI  	0,00	45,46	45,46	0,00	17 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	12	LEV   	0,00	2,77	2,77	0,00	16 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	20	RNW   	0,00	12,60	12,60	0,00	16 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	2	LSPD  	0,00	18,96	18,96	0,00	16 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	5	NVEI  	0,00	44,42	44,42	0,00	16 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	80	INO.UN	0,00	3,64	3,64	0,00	15 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	40	INO.UN	0,00	3,54	3,54	0,00	15 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	10	LSPD  	0,00	19,15	19,15	0,00	15 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	10	NVEI  	0,00	45,95	45,95	0,00	15 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	20	RNW   	0,00	12,72	12,72	0,00	15 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	17	LEV   	0,00	2,83	2,83	0,00	12 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	9	LSPD  	0,00	19,70	19,70	0,00	11 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	50	LEV   	0,00	2,91	2,91	0,00	11 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	109	CTS   	0,00	3,25	3,25	0,00	11 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	3	NVEI  	0,00	48,62	48,62	0,00	11 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	40	RNW   	0,00	12,13	12,13	0,00	11 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	57	CTS   	0,00	2,78	2,78	0,00	10 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	86	INO.UN	0,00	3,44	3,44	0,00	10 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	3	NVEI  	0,00	45,48	45,48	0,00	10 mai 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	2	CTS   	0,00	3,21	3,21	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	103	HR.UN 	0,00	10,19	10,19	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	24	LSPD  	0,00	18,21	18,21	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	150	LEV   	0,00	2,51	2,51	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	50	LEV   	0,00	2,53	2,53	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	16	NVEI  	0,00	42,80	42,80	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	23	SHOP  	0,00	77,67	77,67	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	77,33	77,33	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	4	SHOP  	0,00	77,82	77,82	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	47	HR.UN 	0,00	10,22	10,22	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	80	LSPD  	0,00	18,25	18,25	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,54	2,54	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	16	NVEI  	0,00	41,36	41,36	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	13	SHOP  	0,00	77,51	77,51	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	8	SHOP  	0,00	77,92	77,92	0,00	31 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	INO.UN	0,00	3,46	3,46	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,42	3,42	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	140	HR.UN 	0,00	10,15	10,15	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	160	HR.UN 	0,00	10,18	10,18	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	56	LSPD  	0,00	18,07	18,07	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	28	SHOP  	0,00	80,51	80,51	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	SHOP  	0,00	80,42	80,42	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	SHOP  	0,00	80,63	80,63	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	500	FOOD  	0,00	0,48	0,48	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	130	HR.UN 	0,00	10,15	10,15	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	HR.UN 	0,00	10,16	10,16	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,43	3,43	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LSPD  	0,00	17,69	17,69	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	LSPD  	0,00	18,06	18,06	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	35	NVEI  	0,00	43,08	43,08	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	NVEI  	0,00	43,32	43,32	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	2	SHOP  	0,00	80,76	80,76	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	18	SHOP  	0,00	80,38	80,38	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	2	SHOP  	0,00	81,23	81,23	0,00	30 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,46	3,46	0,00	29 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	INO.UN	0,00	3,50	3,50	0,00	29 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	43,20	43,20	0,00	29 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	500	CTS   	0,00	3,48	3,48	0,00	29 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	600	FOOD  	0,00	0,48	0,48	0,00	29 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,51	3,51	0,00	29 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,42	3,42	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	CTS   	0,00	3,34	3,34	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	400	FOOD  	0,00	0,49	0,49	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	FOOD  	0,00	0,50	0,50	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	21	HR.UN 	0,00	10,21	10,21	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	39	LSPD  	0,00	17,19	17,19	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	LSPD  	0,00	17,51	17,51	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	52	SHOP  	0,00	80,53	80,53	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	SHOP  	0,00	81,36	81,36	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	1	AQN   	0,00	11,28	11,28	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	1	AQN   	0,00	11,28	11,28	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	330	CTS   	0,00	3,41	3,41	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	FOOD  	0,00	0,50	0,50	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	159	HR.UN 	0,00	10,21	10,21	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	HR.UN 	0,00	10,18	10,18	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,45	3,45	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	50	LSPD  	0,00	17,22	17,22	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	26	LSPD  	0,00	17,51	17,51	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	43,19	43,19	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	11	NVEI  	0,00	43,51	43,51	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	SHOP  	0,00	80,53	80,53	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	72	SHOP  	0,00	81,14	81,14	0,00	26 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,22	3,22	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,26	3,26	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	53	AQN   	0,00	11,38	11,38	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	53	AQN   	0,00	11,37	11,37	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	CTS   	0,00	3,26	3,26	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	HR.UN 	0,00	10,25	10,25	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	75	INO.UN	0,00	3,43	3,43	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	47	LSPD  	0,00	17,49	17,49	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	18	LSPD  	0,00	17,76	17,76	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	2	SHOP  	0,00	78,95	78,95	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,42	3,42	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,41	3,41	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	HR.UN 	0,00	10,28	10,28	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	49	LSPD  	0,00	17,52	17,52	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	NVEI  	0,00	43,03	43,03	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	2	SHOP  	0,00	79,39	79,39	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	2	SHOP  	0,00	79,68	79,68	0,00	25 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,19	3,19	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	120	HR.UN 	0,00	10,24	10,24	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	140	INO.UN	0,00	3,46	3,46	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LSPD  	0,00	17,69	17,69	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	1	LEV   	0,00	2,67	2,67	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	29	NVEI  	0,00	42,13	42,13	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	42,30	42,30	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	HR.UN 	0,00	10,26	10,26	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LSPD  	0,00	17,75	17,75	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	13	NVEI  	0,00	42,10	42,10	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	16	NVEI  	0,00	42,24	42,24	0,00	24 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,43	3,43	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,68	2,68	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,59	3,59	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AQN   	0,00	11,62	11,62	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AQN   	0,00	11,61	11,61	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	500	FOOD  	0,00	0,51	0,51	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	52	LSPD  	0,00	18,22	18,22	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	53	LSPD  	0,00	18,45	18,45	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	117	LEV   	0,00	2,69	2,69	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	4	SHOP  	0,00	79,06	79,06	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	14	SHOP  	0,00	83,29	83,29	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,41	3,41	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AQN   	0,00	11,57	11,57	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	AQN   	0,00	11,59	11,59	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,56	3,56	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LSPD  	0,00	18,98	18,98	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	91	LEV   	0,00	2,78	2,78	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	11	NVEI  	0,00	45,13	45,13	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	37	SHOP  	0,00	81,65	81,65	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	80,36	80,36	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	13	SHOP  	0,00	82,41	82,41	0,00	23 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,29	3,29	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,51	3,51	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,28	3,28	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	61	AQN   	0,00	11,46	11,46	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	CTS   	0,00	3,28	3,28	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	HR.UN 	0,00	10,30	10,30	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	LSPD  	0,00	18,37	18,37	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	LEV   	0,00	2,78	2,78	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	SHOP  	0,00	81,54	81,54	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	SHOP  	0,00	81,73	81,73	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,30	3,30	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,42	3,42	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AQN   	0,00	11,40	11,40	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AQN   	0,00	11,43	11,43	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	225	CTS   	0,00	3,36	3,36	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	HR.UN 	0,00	10,32	10,32	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	600	INO.UN	0,00	3,51	3,51	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	120	LEV   	0,00	2,80	2,80	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	11	NVEI  	0,00	43,42	43,42	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	39	SHOP  	0,00	81,50	81,50	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	SHOP  	0,00	82,06	82,06	0,00	19 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,35	3,35	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,54	3,54	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	1	AQN   	0,00	11,36	11,36	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	LSPD  	0,00	17,53	17,53	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	120	LSPD  	0,00	17,87	17,87	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	17	LEV   	0,00	2,82	2,82	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	RNW   	0,00	12,65	12,65	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	CTS   	0,00	3,35	3,35	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,46	3,46	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,55	3,55	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	205	LSPD  	0,00	17,86	17,86	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	185	LSPD  	0,00	17,43	17,43	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	LSPD  	0,00	17,56	17,56	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	120	NVEI  	0,00	43,83	43,83	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	NVEI  	0,00	43,71	43,71	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	RNW   	0,00	12,60	12,60	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	RNW   	0,00	12,71	12,71	0,00	18 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,37	3,37	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,66	3,66	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	500	FOOD  	0,00	0,50	0,50	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	HR.UN 	0,00	10,28	10,28	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	LSPD  	0,00	20,29	20,29	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	160	LSPD  	0,00	19,52	19,52	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	3	SHOP  	0,00	82,01	82,01	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	RNW   	0,00	12,57	12,57	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	RNW   	0,00	12,69	12,69	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	600	CTS   	0,00	3,40	3,40	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	HR.UN 	0,00	10,19	10,19	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	70	HR.UN 	0,00	10,32	10,32	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,57	3,57	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,63	3,63	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	LSPD  	0,00	20,26	20,26	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	90	LSPD  	0,00	19,63	19,63	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	189	LSPD  	0,00	19,65	19,65	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	NVEI  	0,00	45,17	45,17	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	80,33	80,33	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	SHOP  	0,00	81,56	81,56	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	RNW   	0,00	12,60	12,60	0,00	17 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,55	3,55	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,31	3,31	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,55	3,55	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	AQN   	0,00	11,40	11,40	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	50	AQN   	0,00	11,47	11,47	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	INO.UN	0,00	3,55	3,55	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	74	LSPD  	0,00	18,67	18,67	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	LSPD  	0,00	18,87	18,87	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	22	LEV   	0,00	2,77	2,77	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	44,42	44,42	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	1	RNW   	0,00	12,62	12,62	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	TSLA  	0,00	15,46	15,46	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	25	TSLA  	0,00	15,65	15,65	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,32	3,32	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	110	INO.UN	0,00	3,51	3,51	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	50	LSPD  	0,00	18,77	18,77	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	44,14	44,14	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	18	SHOP  	0,00	81,99	81,99	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	SHOP  	0,00	82,80	82,80	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	19	RNW   	0,00	12,65	12,65	0,00	16 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	600	INO.UN	0,00	3,60	3,60	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,13	3,13	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,17	3,17	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	140	AQN   	0,00	11,57	11,57	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AQN   	0,00	11,62	11,62	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	HR.UN 	0,00	10,49	10,49	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	90	LSPD  	0,00	19,23	19,23	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LSPD  	0,00	19,37	19,37	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	NVEI  	0,00	46,01	46,01	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	8	SHOP  	0,00	82,85	82,85	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	14	SHOP  	0,00	83,32	83,32	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	RNW   	0,00	12,61	12,61	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	80	RNW   	0,00	12,72	12,72	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,13	3,13	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	TSLA  	0,00	15,67	15,67	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	AQN   	0,00	11,53	11,53	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AQN   	0,00	11,64	11,64	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,18	3,18	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	HR.UN 	0,00	10,37	10,37	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	800	INO.UN	0,00	3,62	3,62	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	LSPD  	0,00	19,25	19,25	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	LSPD  	0,00	19,34	19,34	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	32	NVEI  	0,00	46,01	46,01	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	49	NVEI  	0,00	45,91	45,91	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	46,08	46,08	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	82,74	82,74	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	24	SHOP  	0,00	82,87	82,87	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	18	SHOP  	0,00	83,13	83,13	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	RNW   	0,00	12,65	12,65	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	140	RNW   	0,00	12,69	12,69	0,00	15 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	AQN   	0,00	11,72	11,72	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	AQN   	0,00	11,78	11,78	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	HR.UN 	0,00	10,65	10,65	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	75	LEV   	0,00	2,82	2,82	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	NVEI  	0,00	47,28	47,28	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	SHOP  	0,00	83,70	83,70	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	AQN   	0,00	11,72	11,72	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	500	CTS   	0,00	3,14	3,14	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,05	3,05	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	HR.UN 	0,00	10,63	10,63	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	LEV   	0,00	2,86	2,86	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	23	NVEI  	0,00	46,61	46,61	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	9	NVEI  	0,00	46,74	46,74	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	SHOP  	0,00	83,34	83,34	0,00	12 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,32	3,32	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,45	3,45	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	400	LEV   	0,00	2,91	2,91	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,32	3,32	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	110	AQN   	0,00	11,65	11,65	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	75	CTS   	0,00	3,01	3,01	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	LSPD  	0,00	19,60	19,60	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	LEV   	0,00	2,92	2,92	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	NVEI  	0,00	48,55	48,55	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	148	NVEI  	0,00	48,88	48,88	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	7	SHOP  	0,00	83,16	83,16	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	6	SHOP  	0,00	83,55	83,55	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	RNW   	0,00	12,20	12,20	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	RNW   	0,00	12,33	12,33	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	70	AQN   	0,00	11,67	11,67	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	CTS   	0,00	2,61	2,61	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	500	CTS   	0,00	3,16	3,16	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LEV   	0,00	2,90	2,90	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	21	NVEI  	0,00	49,01	49,01	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	41	NVEI  	0,00	48,90	48,90	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	1	SHOP  	0,00	83,05	83,05	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	2	SHOP  	0,00	84,05	84,05	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	RNW   	0,00	12,19	12,19	0,00	11 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,43	3,43	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	2,90	2,90	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	2,85	2,85	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	3,02	3,02	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	3,05	3,05	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	95	CTS   	0,00	2,78	2,78	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	LSPD  	0,00	19,78	19,78	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	26	NVEI  	0,00	46,92	46,92	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	RNW   	0,00	12,12	12,12	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	2,76	2,76	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,07	3,07	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	TSLA  	0,00	15,75	15,75	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	160	CTS   	0,00	2,82	2,82	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LSPD  	0,00	19,64	19,64	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LSPD  	0,00	19,68	19,68	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,04	3,04	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,04	3,04	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	82	NVEI  	0,00	47,50	47,50	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	83	NVEI  	0,00	46,97	46,97	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	90	NVEI  	0,00	46,60	46,60	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	12	NVEI  	0,00	46,30	46,30	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	50	NVEI  	0,00	47,33	47,33	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	RNW   	0,00	12,18	12,18	0,00	10 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LSPD  	0,00	19,33	19,33	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	84,60	84,60	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	RNW   	0,00	12,09	12,09	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	RNW   	0,00	12,20	12,20	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	LEV   	0,00	2,97	2,97	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	120	TSLA  	0,00	15,71	15,71	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	80	TSLA  	0,00	15,82	15,82	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,04	3,04	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	140	LSPD  	0,00	19,25	19,25	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	400	LEV   	0,00	2,93	2,93	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,99	2,99	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	54,52	54,52	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	NVEI  	0,00	55,22	55,22	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	83,82	83,82	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	RNW   	0,00	12,26	12,26	0,00	9 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	400	CTS   	0,00	3,49	3,49	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	170	LSPD  	0,00	19,43	19,43	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	53,73	53,73	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	54,46	54,46	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	500	CTS   	0,00	3,50	3,50	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	70	LSPD  	0,00	19,64	19,64	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	NVEI  	0,00	54,62	54,62	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,24	3,24	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,35	3,35	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	54,50	54,50	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	79,90	79,90	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	TSLA  	0,00	15,84	15,84	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	14	LSPD  	0,00	18,79	18,79	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	13	NVEI  	0,00	53,92	53,92	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	13	NVEI  	0,00	54,34	54,34	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	79,71	79,71	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	80,02	80,02	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,19	3,19	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,20	3,20	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LAC   	0,00	25,46	25,46	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	53,58	53,58	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	SHOP  	0,00	78,87	78,87	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	25	SHOP  	0,00	79,19	79,19	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	SHOP  	0,00	78,60	78,60	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	TSLA  	0,00	15,03	15,03	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	TSLA  	0,00	15,16	15,16	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	49	LAC   	0,00	25,26	25,26	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	29	LAC   	0,00	25,43	25,43	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	NVEI  	0,00	54,61	54,61	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	NVEI  	0,00	54,41	54,41	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	54,77	54,77	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	79,10	79,10	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	79,22	79,22	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	25	SHOP  	0,00	79,49	79,49	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	SHOP  	0,00	80,00	80,00	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,05	3,05	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,24	3,24	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	142	INO.UN	0,00	3,06	3,06	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	LSPD  	0,00	17,45	17,45	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	3,03	3,03	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	SHOP  	0,00	63,52	63,52	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	18	SHOP  	0,00	64,05	64,05	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	13	TSLA  	0,00	15,42	15,42	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,22	3,22	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,27	3,27	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	230	INO.UN	0,00	3,10	3,10	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,97	2,97	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,00	3,00	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	63,66	63,66	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	4	SHOP  	0,00	63,82	63,82	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	64,00	64,00	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,93	2,93	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	73	INO.UN	0,00	3,14	3,14	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	LEV   	0,00	2,93	2,93	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	LEV   	0,00	2,94	2,94	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	15	SHOP  	0,00	63,75	63,75	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	53	AMZN  	0,00	12,54	12,54	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,15	3,15	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	TSLA  	0,00	15,31	15,31	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,14	3,14	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	LSPD  	0,00	17,27	17,27	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	4	SHOP  	0,00	63,28	63,28	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,33	3,33	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	CTS   	0,00	3,20	3,20	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	FOOD  	0,00	0,55	0,55	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	75	INO.UN	0,00	3,37	3,37	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	55,05	55,05	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	SHOP  	0,00	65,00	65,00	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	65,67	65,67	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	53	AMZN  	0,00	12,61	12,61	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,01	3,01	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	TSLA  	0,00	15,16	15,16	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	TSLA  	0,00	15,28	15,28	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	120	CTS   	0,00	3,28	3,28	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,30	3,30	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	150	LEV   	0,00	2,96	2,96	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	150	LEV   	0,00	3,04	3,04	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	65,22	65,22	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	21	SHOP  	0,00	65,14	65,14	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	HR.UN 	0,00	10,18	10,18	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	52	LEV   	0,00	2,52	2,52	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	50	LEV   	0,00	2,55	2,55	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	SHOP  	0,00	77,31	77,31	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	18,23	18,23	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	7	NVEI  	0,00	41,37	41,37	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	7	NVEI  	0,00	41,96	41,96	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	SHOP  	0,00	77,68	77,68	0,00	31 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	FOOD  	0,00	0,48	0,48	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	60	INO.UN	0,00	3,42	3,42	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	18,02	18,02	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	9	SHOP  	0,00	80,62	80,62	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	500	FOOD  	0,00	0,48	0,48	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	40	HR.UN 	0,00	10,16	10,16	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	HR.UN 	0,00	10,20	10,20	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	LSPD  	0,00	18,05	18,05	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	43,02	43,02	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	NVEI  	0,00	43,37	43,37	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	11	SHOP  	0,00	80,14	80,14	0,00	30 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,46	3,46	0,00	29 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,51	3,51	0,00	29 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,53	3,53	0,00	29 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	400	FOOD  	0,00	0,48	0,48	0,00	29 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	360	CTS   	0,00	3,49	3,49	0,00	29 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	500	FOOD  	0,00	0,48	0,48	0,00	29 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,38	3,38	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	60	CTS   	0,00	3,40	3,40	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	42	HR.UN 	0,00	10,19	10,19	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	39	LSPD  	0,00	17,14	17,14	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	17,49	17,49	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	16	AQN   	0,00	11,18	11,18	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	16	AQN   	0,00	11,24	11,24	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,41	3,41	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	300	CTS   	0,00	3,42	3,42	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	48	HR.UN 	0,00	10,20	10,20	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	17,22	17,22	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	13	LSPD  	0,00	17,50	17,50	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	43,46	43,46	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	80,91	80,91	0,00	26 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	200	LEV   	0,00	2,76	2,76	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	18,16	18,16	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	19,01	19,01	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	43	LEV   	0,00	2,75	2,75	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	LEV   	0,00	2,69	2,69	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	HR.UN 	0,00	10,24	10,24	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	3	HR.UN 	0,00	10,36	10,36	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	INO.UN	0,00	3,56	3,56	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	LSPD  	0,00	18,19	18,19	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	3	SHOP  	0,00	83,35	83,35	0,00	23 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,29	3,29	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,50	3,50	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	INO.UN	0,00	3,51	3,51	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	73	INO.UN	0,00	3,50	3,50	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	28	LSPD  	0,00	18,41	18,41	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	LEV   	0,00	2,79	2,79	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	SHOP  	0,00	81,26	81,26	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	81,73	81,73	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,52	3,52	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	15	SHOP  	0,00	81,39	81,39	0,00	19 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	200	CTS   	0,00	3,35	3,35	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	80	LSPD  	0,00	17,41	17,41	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	LSPD  	0,00	17,45	17,45	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	21	RNW   	0,00	12,60	12,60	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	21	RNW   	0,00	12,66	12,66	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	CTS   	0,00	3,47	3,47	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,44	3,44	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	208	LSPD  	0,00	17,73	17,73	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	LSPD  	0,00	18,15	18,15	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	27	NVEI  	0,00	43,14	43,14	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	NVEI  	0,00	43,58	43,58	0,00	18 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	98	LSPD  	0,00	19,74	19,74	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	45,00	45,00	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	81,48	81,48	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	CTS   	0,00	3,42	3,42	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	60	LSPD  	0,00	20,26	20,26	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	130	LSPD  	0,00	19,95	19,95	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	80,33	80,33	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	16	SHOP  	0,00	81,87	81,87	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	RNW   	0,00	12,59	12,59	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	RNW   	0,00	12,72	12,72	0,00	17 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	77	INO.UN	0,00	3,49	3,49	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	50	INO.UN	0,00	3,53	3,53	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	8	LSPD  	0,00	18,84	18,84	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	6	SHOP  	0,00	82,00	82,00	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	82,29	82,29	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	TSLA  	0,00	15,64	15,64	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	AQN   	0,00	11,48	11,48	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,32	3,32	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	LSPD  	0,00	18,78	18,78	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	NVEI  	0,00	44,36	44,36	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	81,78	81,78	0,00	16 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	19,07	19,07	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	60	RNW   	0,00	12,70	12,70	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	TSLA  	0,00	15,56	15,56	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,63	3,63	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	LSPD  	0,00	19,24	19,24	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	82,72	82,72	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	83,18	83,18	0,00	15 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	AQN   	0,00	11,81	11,81	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	15	LEV   	0,00	2,78	2,78	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	83,82	83,82	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	RNW   	0,00	12,06	12,06	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	LEV   	0,00	2,84	2,84	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	AQN   	0,00	11,75	11,75	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,20	3,20	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	8	NVEI  	0,00	47,11	47,11	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	83,46	83,46	0,00	12 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	CTS   	0,00	3,47	3,47	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	LEV   	0,00	2,90	2,90	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	40	AQN   	0,00	11,68	11,68	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	6	INO.UN	0,00	3,40	3,40	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	NVEI  	0,00	45,97	45,97	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	SHOP  	0,00	83,06	83,06	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	3	SHOP  	0,00	83,50	83,50	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	45	RNW   	0,00	12,16	12,16	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	40	AQN   	0,00	11,75	11,75	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	200	CTS   	0,00	3,31	3,31	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	250	CTS   	0,00	3,17	3,17	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	106	INO.UN	0,00	3,45	3,45	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	NVEI  	0,00	47,98	47,98	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	SHOP  	0,00	83,20	83,20	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	3	SHOP  	0,00	83,50	83,50	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	RNW   	0,00	12,33	12,33	0,00	11 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	2,85	2,85	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	78	CTS   	0,00	2,74	2,74	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	28	CTS   	0,00	2,84	2,84	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	19,63	19,63	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	LSPD  	0,00	19,88	19,88	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	200	CTS   	0,00	2,77	2,77	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,43	3,43	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	19,78	19,78	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	LEV   	0,00	3,04	3,04	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	65	NVEI  	0,00	47,55	47,55	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	NVEI  	0,00	46,52	46,52	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	30	RNW   	0,00	12,20	12,20	0,00	10 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	40	LSPD  	0,00	19,29	19,29	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	55,16	55,16	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	RNW   	0,00	12,11	12,11	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	TSLA  	0,00	15,73	15,73	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	50	TSLA  	0,00	15,82	15,82	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	60	LSPD  	0,00	19,21	19,21	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	300	LEV   	0,00	2,94	2,94	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	LEV   	0,00	2,97	2,97	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	54,52	54,52	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	55,20	55,20	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	RNW   	0,00	12,25	12,25	0,00	9 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	19,38	19,38	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	LSPD  	0,00	19,60	19,60	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	53,69	53,69	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	30	NVEI  	0,00	54,64	54,64	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	4	SHOP  	0,00	79,86	79,86	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	TSLA  	0,00	15,84	15,84	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,33	3,33	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	LSPD  	0,00	18,75	18,75	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	79,65	79,65	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	SHOP  	0,00	80,02	80,02	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	54,57	54,57	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	16	SHOP  	0,00	77,67	77,67	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	18	LAC   	0,00	25,26	25,26	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	18	LAC   	0,00	25,40	25,40	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	12	NVEI  	0,00	54,75	54,75	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	2	NVEI  	0,00	54,80	54,80	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	19	SHOP  	0,00	78,94	78,94	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	SHOP  	0,00	78,87	78,87	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	25	SHOP  	0,00	80,04	80,04	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,24	3,24	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,05	3,05	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,10	3,10	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	120	FOOD  	0,00	0,53	0,53	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	124	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	77	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	LSPD  	0,00	17,46	17,46	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	50	LEV   	0,00	2,99	2,99	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	SHOP  	0,00	62,99	62,99	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	TSLA  	0,00	15,12	15,12	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	53	INO.UN	0,00	3,13	3,13	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	9	SHOP  	0,00	63,62	63,62	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	6	SHOP  	0,00	64,27	64,27	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	42	INO.UN	0,00	3,15	3,15	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	7	SHOP  	0,00	63,76	63,76	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	22	AMZN  	0,00	12,51	12,51	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	14,97	14,97	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	230	INO.UN	0,00	3,15	3,15	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	LSPD  	0,00	17,13	17,13	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	2	SHOP  	0,00	63,32	63,32	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	CTS   	0,00	3,21	3,21	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	52	INO.UN	0,00	3,36	3,36	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LEV   	0,00	3,07	3,07	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	6	SHOP  	0,00	64,48	64,48	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	12	AMZN  	0,00	12,64	12,64	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	15,17	15,17	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	30	TSLA  	0,00	15,24	15,24	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	CTS   	0,00	3,28	3,28	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,30	3,30	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LEV   	0,00	3,01	3,01	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	55,09	55,09	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	8	SHOP  	0,00	65,38	65,38	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	3	SHOP  	0,00	65,61	65,61	0,00	1 mai 2023                       
                """;            
     private static String INPUT_DATAX_A = """
        000009048000	5KVXEZ7	Exécuté	Vente	2	LEV   	0,00	2,96	2,96	0,00	28 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	2	LEV   	0,00	2,91	2,91	0,00	24 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	20	AQN   	0,00	10,82	10,82	0,00	19 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	66	INO.UN	0,00	3,46	3,46	0,00	19 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	9	LSPD  	0,00	18,64	18,64	0,00	19 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	109	CTS   	0,00	3,84	3,84	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	100	LEV   	0,00	2,88	2,88	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	20	AQN   	0,00	10,80	10,80	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	50	CTS   	0,00	3,86	3,86	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	109	CTS   	0,00	3,90	3,90	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	114	INO.UN	0,00	3,54	3,54	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	100	LEV   	0,00	3,06	3,06	0,00	18 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	7	TSLA  	0,00	17,34	17,34	0,00	17 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	7	TSLA  	0,00	17,58	17,58	0,00	17 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	100	LEV   	0,00	2,63	2,63	0,00	17 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	2	SHOP  	0,00	64,44	64,44	0,00	17 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	6	INO.UN	0,00	3,71	3,71	0,00	5 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	50	LEV   	0,00	2,44	2,44	0,00	5 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	20	INO.UN	0,00	3,74	3,74	0,00	5 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	50	LEV   	0,00	2,42	2,42	0,00	5 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	2	SHOP  	0,00	62,94	62,94	0,00	5 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	40	LSPD  	0,00	19,84	19,84	0,00	4 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Vente	10	LSPD  	0,00	19,99	19,99	0,00	4 avril 2023 
        000009048000	5KVXEZ7	Exécuté	Achat	10	LSPD  	0,00	20,03	20,03	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	170	LSPD  	0,00	19,43	19,43	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	53,73	53,73	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	54,46	54,46	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	500	CTS   	0,00	3,50	3,50	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	70	LSPD  	0,00	19,64	19,64	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	NVEI  	0,00	54,62	54,62	0,00	8 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,24	3,24	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,35	3,35	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	54,50	54,50	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	79,90	79,90	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	TSLA  	0,00	15,84	15,84	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	14	LSPD  	0,00	18,79	18,79	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	13	NVEI  	0,00	53,92	53,92	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	13	NVEI  	0,00	54,34	54,34	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	79,71	79,71	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	80,02	80,02	0,00	5 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,19	3,19	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,20	3,20	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LAC   	0,00	25,46	25,46	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	53,58	53,58	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	SHOP  	0,00	78,87	78,87	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	25	SHOP  	0,00	79,19	79,19	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	SHOP  	0,00	78,60	78,60	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	TSLA  	0,00	15,03	15,03	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	TSLA  	0,00	15,16	15,16	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	49	LAC   	0,00	25,26	25,26	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	29	LAC   	0,00	25,43	25,43	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	NVEI  	0,00	54,61	54,61	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	NVEI  	0,00	54,41	54,41	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	54,77	54,77	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	79,10	79,10	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	79,22	79,22	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	25	SHOP  	0,00	79,49	79,49	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	35	SHOP  	0,00	80,00	80,00	0,00	4 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,05	3,05	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,24	3,24	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	142	INO.UN	0,00	3,06	3,06	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	LSPD  	0,00	17,45	17,45	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	3,03	3,03	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	SHOP  	0,00	63,52	63,52	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	18	SHOP  	0,00	64,05	64,05	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	13	TSLA  	0,00	15,42	15,42	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,22	3,22	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,27	3,27	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	230	INO.UN	0,00	3,10	3,10	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,97	2,97	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,00	3,00	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	25	SHOP  	0,00	63,66	63,66	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	4	SHOP  	0,00	63,82	63,82	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	64,00	64,00	0,00	3 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,93	2,93	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	73	INO.UN	0,00	3,14	3,14	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	LEV   	0,00	2,93	2,93	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	LEV   	0,00	2,94	2,94	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	15	SHOP  	0,00	63,75	63,75	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	53	AMZN  	0,00	12,54	12,54	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,15	3,15	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	TSLA  	0,00	15,31	15,31	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,14	3,14	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	LSPD  	0,00	17,27	17,27	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	4	SHOP  	0,00	63,28	63,28	0,00	2 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,33	3,33	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	CTS   	0,00	3,20	3,20	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	FOOD  	0,00	0,55	0,55	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	75	INO.UN	0,00	3,37	3,37	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	55,05	55,05	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	SHOP  	0,00	65,00	65,00	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	65,67	65,67	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	53	AMZN  	0,00	12,61	12,61	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,01	3,01	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	TSLA  	0,00	15,16	15,16	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	TSLA  	0,00	15,28	15,28	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	120	CTS   	0,00	3,28	3,28	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,30	3,30	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	150	LEV   	0,00	2,96	2,96	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Vente	150	LEV   	0,00	3,04	3,04	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	65,22	65,22	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	21	SHOP  	0,00	65,14	65,14	0,00	1 mai 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AQN   	0,00	11,37	11,37	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	55,00	55,00	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	GOOG  	0,00	18,08	18,08	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	GOOG  	0,00	18,28	18,28	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AMZN  	0,00	12,75	12,75	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AMZN  	0,00	12,89	12,89	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	TSLA  	0,00	14,83	14,83	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	17	TSLA  	0,00	15,33	15,33	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AQN   	0,00	11,44	11,44	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	149	CTS   	0,00	3,33	3,33	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	374	LEV   	0,00	2,98	2,98	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	54,96	54,96	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	55,19	55,19	0,00	28 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	CTS   	0,00	3,26	3,26	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,27	3,27	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,29	3,29	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	14	AQN   	0,00	11,33	11,33	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	14	AQN   	0,00	11,40	11,40	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	CTS   	0,00	3,32	3,32	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	98	INO.UN	0,00	3,40	3,40	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	39	LSPD  	0,00	17,77	17,77	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	57	LEV   	0,00	2,80	2,80	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	17	GOOG  	0,00	18,14	18,14	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,23	3,23	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	85	TSLA  	0,00	14,82	14,82	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,28	3,28	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,29	3,29	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,44	3,44	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	LSPD  	0,00	17,78	17,78	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,81	2,81	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	2	NVEI  	0,00	52,85	52,85	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	NVEI  	0,00	53,74	53,74	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	64,41	64,41	0,00	27 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,80	2,80	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AQN   	0,00	11,39	11,39	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	50	CTS   	0,00	3,32	3,32	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	FOOD  	0,00	0,55	0,55	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	19	LSPD  	0,00	17,42	17,42	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	114	LEV   	0,00	2,82	2,82	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	3	NVEI  	0,00	53,46	53,46	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	17	GOOG  	0,00	17,92	17,92	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	16	GOOG  	0,00	18,06	18,06	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AMZN  	0,00	12,83	12,83	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,33	3,33	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	TSLA  	0,00	14,52	14,52	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	110	CTS   	0,00	3,34	3,34	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	6	LSPD  	0,00	17,62	17,62	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LSPD  	0,00	17,75	17,75	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	103	LEV   	0,00	2,80	2,80	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	3	SHOP  	0,00	64,19	64,19	0,00	26 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,25	3,25	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,85	2,85	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	18	AQN   	0,00	11,42	11,42	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	18	AQN   	0,00	11,45	11,45	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	97	CTS   	0,00	3,28	3,28	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	CTS   	0,00	3,37	3,37	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	42	INO.UN	0,00	3,38	3,38	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	50	INO.UN	0,00	3,44	3,44	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	54	LSPD  	0,00	17,66	17,66	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	26	LSPD  	0,00	17,73	17,73	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	3	SHOP  	0,00	63,60	63,60	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	16	GOOG  	0,00	17,82	17,82	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AMZN  	0,00	12,51	12,51	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,85	2,85	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	3	TSLA  	0,00	14,98	14,98	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	TSLA  	0,00	15,28	15,28	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	18	CTS   	0,00	3,34	3,34	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,39	3,39	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	49	LSPD  	0,00	17,67	17,67	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	17	LSPD  	0,00	17,87	17,87	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	3	NVEI  	0,00	54,26	54,26	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	3	NVEI  	0,00	54,44	54,44	0,00	25 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AQN   	0,00	11,52	11,52	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	60	AQN   	0,00	11,48	11,48	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	74	CTS   	0,00	3,46	3,46	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	95	INO.UN	0,00	3,43	3,43	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	3	LSPD  	0,00	17,89	17,89	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	NVEI  	0,00	56,86	56,86	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	64,41	64,41	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	64,86	64,86	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,90	2,90	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	2,94	2,94	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	19	TSLA  	0,00	15,25	15,25	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AQN   	0,00	11,57	11,57	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	140	AQN   	0,00	11,53	11,53	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	LSPD  	0,00	18,15	18,15	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	LEV   	0,00	2,90	2,90	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	15	NVEI  	0,00	56,53	56,53	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	SHOP  	0,00	65,04	65,04	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	65,31	65,31	0,00	24 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	95	INO.UN	0,00	3,44	3,44	0,00	21 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	65,58	65,58	0,00	21 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,69	3,69	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	GOOG  	0,00	18,04	18,04	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	43	TSLA  	0,00	15,34	15,34	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	166	CTS   	0,00	3,69	3,69	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	LEV   	0,00	3,03	3,03	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	NVEI  	0,00	56,71	56,71	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	SHOP  	0,00	65,32	65,32	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	65,52	65,52	0,00	20 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	NVEI  	0,00	56,91	56,91	0,00	19 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	AQN   	0,00	10,85	10,85	0,00	19 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,94	2,94	0,00	19 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,08	3,08	0,00	19 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	7	NVEI  	0,00	56,55	56,55	0,00	19 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	7	NVEI  	0,00	56,67	56,67	0,00	19 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,09	3,09	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,23	3,23	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,53	3,53	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,31	3,31	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	AQN   	0,00	10,84	10,84	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	CTS   	0,00	3,87	3,87	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	CTS   	0,00	3,89	3,89	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	GOOG  	0,00	17,87	17,87	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	AMZN  	0,00	12,41	12,41	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AMZN  	0,00	12,48	12,48	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,53	3,53	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	3,24	3,24	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,36	3,36	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	AQN   	0,00	10,80	10,80	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,54	3,54	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	3,26	3,26	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	800	LEV   	0,00	3,18	3,18	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	410	LEV   	0,00	3,21	3,21	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	56,12	56,12	0,00	18 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,90	3,90	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	400	CTS   	0,00	3,91	3,91	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	AQN   	0,00	11,47	11,47	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	300	FOOD  	0,00	0,56	0,56	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	SHOP  	0,00	63,94	63,94	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	15	SHOP  	0,00	64,80	64,80	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	TSLA  	0,00	17,48	17,48	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	TSLA  	0,00	17,76	17,76	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	AQN   	0,00	11,52	11,52	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,91	3,91	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	15	NVEI  	0,00	56,15	56,15	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	22	NVEI  	0,00	57,08	57,08	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	13	SHOP  	0,00	64,30	64,30	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	34	SHOP  	0,00	64,51	64,51	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	13	SHOP  	0,00	64,60	64,60	0,00	17 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	4,01	4,01	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,99	3,99	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	AQN   	0,00	11,62	11,62	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LSPD  	0,00	18,87	18,87	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	7	NVEI  	0,00	57,49	57,49	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	9	SHOP  	0,00	61,56	61,56	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,57	3,57	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	TSLA  	0,00	17,18	17,18	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	TSLA  	0,00	17,42	17,42	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	AQN   	0,00	11,57	11,57	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	400	CTS   	0,00	4,03	4,03	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,63	3,63	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,66	3,66	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LSPD  	0,00	18,90	18,90	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	15	NVEI  	0,00	57,40	57,40	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	57,53	57,53	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	60,91	60,91	0,00	14 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	4,00	4,00	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	4,03	4,03	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LAC   	0,00	26,62	26,62	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	NVEI  	0,00	57,83	57,83	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	NVEI  	0,00	58,00	58,00	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	61,30	61,30	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	SHOP  	0,00	61,69	61,69	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	50	AMZN  	0,00	12,20	12,20	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	16	TSLA  	0,00	17,11	17,11	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	16	TSLA  	0,00	17,29	17,29	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	4,04	4,04	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	4,03	4,03	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,56	3,56	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	LSPD  	0,00	19,30	19,30	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	LAC   	0,00	26,44	26,44	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	LAC   	0,00	26,57	26,57	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	NVEI  	0,00	57,84	57,84	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	25	NVEI  	0,00	57,93	57,93	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	15	SHOP  	0,00	61,12	61,12	0,00	13 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	INO.UN	0,00	3,50	3,50	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,27	2,27	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	69	INO.UN	0,00	3,50	3,50	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	12	LSPD  	0,00	18,96	18,96	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	83	LEV   	0,00	2,28	2,28	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	5	NVEI  	0,00	56,65	56,65	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	60,94	60,94	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	50	AMZN  	0,00	12,07	12,07	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	50	LEV   	0,00	2,30	2,30	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	30	LAC   	0,00	26,25	26,25	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	26	NVEI  	0,00	56,79	56,79	0,00	12 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	600	FOOD  	0,00	0,57	0,57	0,00	11 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	2	SHOP  	0,00	60,76	60,76	0,00	11 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	32	AMZN  	0,00	12,16	12,16	0,00	11 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	32	AMZN  	0,00	12,19	12,19	0,00	11 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	SHOP  	0,00	60,65	60,65	0,00	11 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	4	SHOP  	0,00	61,08	61,08	0,00	11 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	23	INO.UN	0,00	3,40	3,40	0,00	10 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	4	NVEI  	0,00	53,56	53,56	0,00	10 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	9	SRU.UN	0,00	26,10	26,10	0,00	10 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	46	INO.UN	0,00	3,39	3,39	0,00	10 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	2,34	2,34	0,00	10 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	4	NVEI  	0,00	53,81	53,81	0,00	10 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	CTS   	0,00	3,80	3,80	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	400	LEV   	0,00	2,35	2,35	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	LEV   	0,00	2,35	2,35	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,36	2,36	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	50	CTS   	0,00	3,68	3,68	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	163	INO.UN	0,00	3,46	3,46	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	277	LEV   	0,00	2,33	2,33	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	LEV   	0,00	2,36	2,36	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	50	CTS   	0,00	3,67	3,67	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,70	3,70	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	253	INO.UN	0,00	3,45	3,45	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	200	INO.UN	0,00	3,52	3,52	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	LSPD  	0,00	18,47	18,47	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	SRU.UN	0,00	26,19	26,19	0,00	6 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,63	3,63	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,77	3,77	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,80	3,80	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,37	2,37	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	14	INO.UN	0,00	3,55	3,55	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	9	LSPD  	0,00	18,73	18,73	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	30	LSPD  	0,00	18,89	18,89	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	LEV   	0,00	2,37	2,37	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	22	SHOP  	0,00	61,26	61,26	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	SRU.UN	0,00	26,28	26,28	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	22	TSLA  	0,00	17,41	17,41	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	22	TSLA  	0,00	17,44	17,44	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	3,80	3,80	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	100	CTS   	0,00	3,80	3,80	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	370	INO.UN	0,00	3,67	3,67	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,74	3,74	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	71	LSPD  	0,00	18,63	18,63	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	11	NVEI  	0,00	55,50	55,50	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	5	SHOP  	0,00	61,77	61,77	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	61,96	61,96	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	SRU.UN	0,00	26,23	26,23	0,00	5 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,85	3,85	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	CTS   	0,00	3,96	3,96	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AQN   	0,00	11,32	11,32	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	58,03	58,03	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	SHOP  	0,00	63,76	63,76	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	INO.UN	0,00	3,83	3,83	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	60	TSLA  	0,00	18,11	18,11	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	200	INO.UN	0,00	3,84	3,84	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	NVEI  	0,00	57,70	57,70	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SHOP  	0,00	64,18	64,18	0,00	4 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	CTS   	0,00	4,01	4,01	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	AQN   	0,00	11,18	11,18	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AQN   	0,00	11,23	11,23	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	LSPD  	0,00	20,05	20,05	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	LSPD  	0,00	20,05	20,05	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	20	NVEI  	0,00	57,18	57,18	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	NVEI  	0,00	57,46	57,46	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	10	SRU.UN	0,00	26,50	26,50	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	TSLA  	0,00	18,12	18,12	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	AQN   	0,00	11,22	11,22	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	40	AQN   	0,00	11,30	11,30	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	80	LSPD  	0,00	19,82	19,82	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	20	LSPD  	0,00	20,12	20,12	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	100	LEV   	0,00	2,48	2,48	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Achat	40	NVEI  	0,00	57,46	57,46	0,00	3 avril 2023 
        000009048000	5KVXER7	Exécuté	Vente	10	SRU.UN	0,00	26,61	26,61	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	LSPD  	0,00	19,60	19,60	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	53,69	53,69	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	30	NVEI  	0,00	54,64	54,64	0,00	8 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	4	SHOP  	0,00	79,86	79,86	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	TSLA  	0,00	15,84	15,84	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,33	3,33	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	LSPD  	0,00	18,75	18,75	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	79,65	79,65	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	SHOP  	0,00	80,02	80,02	0,00	5 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	54,57	54,57	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	16	SHOP  	0,00	77,67	77,67	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	18	LAC   	0,00	25,26	25,26	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	18	LAC   	0,00	25,40	25,40	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	12	NVEI  	0,00	54,75	54,75	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	2	NVEI  	0,00	54,80	54,80	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	19	SHOP  	0,00	78,94	78,94	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	SHOP  	0,00	78,87	78,87	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	25	SHOP  	0,00	80,04	80,04	0,00	4 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,24	3,24	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,05	3,05	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,10	3,10	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	120	FOOD  	0,00	0,53	0,53	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	124	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	77	INO.UN	0,00	3,08	3,08	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	LSPD  	0,00	17,46	17,46	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	50	LEV   	0,00	2,99	2,99	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	SHOP  	0,00	62,99	62,99	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,21	3,21	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	TSLA  	0,00	15,12	15,12	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	53	INO.UN	0,00	3,13	3,13	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	9	SHOP  	0,00	63,62	63,62	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	6	SHOP  	0,00	64,27	64,27	0,00	3 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	42	INO.UN	0,00	3,15	3,15	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	7	SHOP  	0,00	63,76	63,76	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	22	AMZN  	0,00	12,51	12,51	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	14,97	14,97	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	230	INO.UN	0,00	3,15	3,15	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	LSPD  	0,00	17,13	17,13	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	2	SHOP  	0,00	63,32	63,32	0,00	2 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	CTS   	0,00	3,21	3,21	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	52	INO.UN	0,00	3,36	3,36	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LEV   	0,00	3,07	3,07	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	6	SHOP  	0,00	64,48	64,48	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	12	AMZN  	0,00	12,64	12,64	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	15,17	15,17	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	30	TSLA  	0,00	15,24	15,24	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	CTS   	0,00	3,28	3,28	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,30	3,30	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LEV   	0,00	3,01	3,01	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	55,09	55,09	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Achat	8	SHOP  	0,00	65,38	65,38	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	3	SHOP  	0,00	65,61	65,61	0,00	1 mai 2023 
        000009048000	5KVXEY9	Exécuté	Vente	46	CTS   	0,00	3,33	3,33	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	NVEI  	0,00	55,00	55,00	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	55,18	55,18	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	AMZN  	0,00	12,78	12,78	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	AMZN  	0,00	12,90	12,90	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	TSLA  	0,00	15,44	15,44	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,34	3,34	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	CTS   	0,00	3,34	3,34	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	61	LEV   	0,00	2,97	2,97	0,00	28 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	CTS   	0,00	3,27	3,27	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	AQN   	0,00	11,34	11,34	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	AQN   	0,00	11,40	11,40	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	46	CTS   	0,00	3,28	3,28	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	66	LEV   	0,00	2,79	2,79	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	3	NVEI  	0,00	53,05	53,05	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	11	GOOG  	0,00	18,18	18,18	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	AQN   	0,00	11,42	11,42	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,44	3,44	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	17,77	17,77	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	2	NVEI  	0,00	52,96	52,96	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	53,49	53,49	0,00	27 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	45	LEV   	0,00	2,83	2,83	0,00	26 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	11	GOOG  	0,00	18,00	18,00	0,00	26 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	AMZN  	0,00	12,86	12,86	0,00	26 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	11	LSPD  	0,00	17,76	17,76	0,00	26 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,40	3,40	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	68	CTS   	0,00	3,25	3,25	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	INO.UN	0,00	3,40	3,40	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	AMZN  	0,00	12,51	12,51	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	2	INO.UN	0,00	3,38	3,38	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	48	LSPD  	0,00	17,64	17,64	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	17,73	17,73	0,00	25 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	2	CTS   	0,00	3,49	3,49	0,00	24 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	82	INO.UN	0,00	3,44	3,44	0,00	24 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	95	INO.UN	0,00	3,42	3,42	0,00	24 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	NVEI  	0,00	56,44	56,44	0,00	24 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	57	INO.UN	0,00	3,44	3,44	0,00	21 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	8	SHOP  	0,00	65,74	65,74	0,00	21 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	60	LEV   	0,00	2,92	2,92	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	7	SHOP  	0,00	65,55	65,55	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	GOOG  	0,00	18,00	18,00	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	TSLA  	0,00	15,31	15,31	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	110	CTS   	0,00	3,70	3,70	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	60	LEV   	0,00	3,01	3,01	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	56,85	56,85	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	15	SHOP  	0,00	65,30	65,30	0,00	20 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	LEV   	0,00	3,06	3,06	0,00	19 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	NVEI  	0,00	56,74	56,74	0,00	19 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	NVEI  	0,00	57,00	57,00	0,00	19 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,55	3,55	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	14	INO.UN	0,00	3,55	3,55	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	LEV   	0,00	3,04	3,04	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	50	LEV   	0,00	3,15	3,15	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	GOOG  	0,00	17,90	17,90	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	AQN   	0,00	10,80	10,80	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	AQN   	0,00	10,84	10,84	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	LEV   	0,00	3,25	3,25	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	365	LEV   	0,00	3,15	3,15	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	NVEI  	0,00	55,94	55,94	0,00	18 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	70	CTS   	0,00	3,91	3,91	0,00	17 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	17,60	17,60	0,00	17 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	TSLA  	0,00	17,82	17,82	0,00	17 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	8	LSPD  	0,00	18,84	18,84	0,00	17 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	57,18	57,18	0,00	17 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	64,30	64,30	0,00	17 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	86	INO.UN	0,00	3,59	3,59	0,00	14 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	25	LSPD  	0,00	18,82	18,82	0,00	14 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	100	INO.UN	0,00	3,66	3,66	0,00	14 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	57,72	57,72	0,00	14 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	61,25	61,25	0,00	14 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	40	INO.UN	0,00	3,56	3,56	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	9	NVEI  	0,00	57,82	57,82	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	57,41	57,41	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	61,29	61,29	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	15	AMZN  	0,00	12,21	12,21	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	LEV   	0,00	2,26	2,26	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	4	NVEI  	0,00	57,16	57,16	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	23	NVEI  	0,00	58,04	58,04	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	61,03	61,03	0,00	13 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	LEV   	0,00	2,31	2,31	0,00	12 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	15	AMZN  	0,00	12,06	12,06	0,00	12 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	NVEI  	0,00	56,84	56,84	0,00	12 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	200	FOOD  	0,00	0,57	0,57	0,00	11 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	2	LEV   	0,00	2,36	2,36	0,00	11 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	1	SHOP  	0,00	60,17	60,17	0,00	11 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	1	SHOP  	0,00	60,16	60,16	0,00	11 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	2	SHOP  	0,00	60,39	60,39	0,00	11 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	40	INO.UN	0,00	3,71	3,71	0,00	5 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	18,78	18,78	0,00	5 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	72	LEV   	0,00	2,37	2,37	0,00	5 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	100	INO.UN	0,00	3,79	3,79	0,00	5 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	SRU.UN	0,00	26,31	26,31	0,00	5 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	AQN   	0,00	11,31	11,31	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	50	CTS   	0,00	3,95	3,95	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	18,12	18,12	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	AQN   	0,00	11,34	11,34	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	200	INO.UN	0,00	3,83	3,83	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	19,96	19,96	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	20,10	20,10	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	NVEI  	0,00	58,04	58,04	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	5	SHOP  	0,00	63,75	63,75	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	5	SHOP  	0,00	64,18	64,18	0,00	4 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	LSPD  	0,00	19,94	19,94	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	LSPD  	0,00	20,12	20,12	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	10	NVEI  	0,00	57,18	57,18	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	10	NVEI  	0,00	57,46	57,46	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Vente	20	SRU.UN	0,00	26,55	26,55	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	TSLA  	0,00	18,12	18,12	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	AQN   	0,00	11,18	11,18	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	80	LSPD  	0,00	19,92	19,92	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	30	NVEI  	0,00	57,36	57,36	0,00	3 avril 2023 
        000009048000	5KVXEY9	Exécuté	Achat	20	SRU.UN	0,00	26,38	26,38	0,00	3 avril 2023 
     """;

     private static String INPUT_DATAX = """
         230613058276	5KVXEY9	Exécuté	Achat	5	SHOP-C	85,00	85,00	84,99	425,00	13 juin 2023	
         230613057040	5KVXER7	Exécuté	Achat	100	CTS-C	3,24	3,235	3,25	323,50	13 juin 2023	
         230613056969	5KVXEY9	Exécuté	Vente	200	INO.UN-C	3,31	3,31	3,30	662,00	13 juin 2023	
         230613056553	5KVXER7	Exécuté	Vente	5	SHOP-C	85,12	85,13	84,99	425,65	13 juin 2023	
         230613055943	5KVXEY9	Exécuté	Achat	300	CTS-C	3,25	3,25	3,25	975,00	13 juin 2023	
         230613055847	5KVXEY9	Exécuté	Vente	300	INO.UN-C	3,31	3,31	3,30	993,00	13 juin 2023	
         230613054752	5KVXEY9	Exécuté	Achat	4	CTS-C	3,26	3,26	3,25	13,04	13 juin 2023	
         230613054668	5KVXEY9	Exécuté	Achat	18	CTS-C	3,26	3,26	3,25	58,68	13 juin 2023	
         230613054314	5KVXER7	Exécuté	Achat	150	CTS-C	3,26	3,25667	3,25	488,50005	13 juin 2023	
         230613054242	5KVXER7	Exécuté	Vente	5	SHOP-C	85,18	85,18	84,99	425,90	13 juin 2023	
         230613051443	5KVXER7	Exécuté	Achat	10	NVEI-C	39,62	39,62	40,01	396,20	13 juin 2023	
         230613051406	5KVXER7	Exécuté	Achat	200	CTS-C	3,26	3,26	3,25	652,00	13 juin 2023	
         230613051124	5KVXER7	Exécuté	Vente	5	SHOP-C	85,13	85,13	84,99	425,65	13 juin 2023	
         230613050999	5KVXER7	Exécuté	Achat	5	NVEI-C	39,62	39,62	40,01	198,10	13 juin 2023	
         230613050621	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,31	3,31	3,30	662,00	13 juin 2023	
         230613050472	5KVXER7	Exécuté	Achat	100	CTS-C	3,27	3,265	3,25	326,50	13 juin 2023	
         230613048559	5KVXEZ7	Exécuté	Achat	15	CTS-C	3,28	3,28	3,25	49,20	13 juin 2023	
         230613048270	5KVXEY9	Exécuté	Achat	50	CTS-C	3,28	3,28	3,25	164,00	13 juin 2023	
         230613048239	5KVXER7	Exécuté	Achat	200	CTS-C	3,27	3,27	3,25	654,00	13 juin 2023	
         230613048002	5KVXER7	Exécuté	Achat	200	CTS-C	3,29	3,285	3,25	657,00	13 juin 2023	
         230613046929	5KVXER7	Exécuté	Achat	5	SHOP-C	84,44	84,44	84,99	422,20	13 juin 2023	
         230613043084	5KVXER7	Exécuté	Vente	5	SHOP-C	85,01	85,02	84,99	425,10	13 juin 2023	
         230613043002	5KVXEY9	Exécuté	Achat	100	CTS-C	3,31	3,305	3,25	330,50	13 juin 2023	
         230613042720	5KVXEZ7	Exécuté	Achat	5	NVEI-C	39,82	39,82	40,01	199,10	13 juin 2023	
         230613042687	5KVXEZ7	Exécuté	Achat	5	NVEI-C	39,83	39,83	40,01	199,15	13 juin 2023	
         230613042413	5KVXER7	Exécuté	Achat	10	SHOP-C	84,75	84,75	84,99	847,50	13 juin 2023	
         230613042027	5KVXER7	Exécuté	Vente	20	NWC-C	34,43	34,43	34,31	688,60	13 juin 2023	
         230613041832	5KVXER7	Exécuté	Vente	10	SHOP-C	85,19	85,22	84,99	852,20	13 juin 2023	
         230613041226	5KVXER7	Exécuté	Achat	5	SHOP-C	84,68	84,67	84,99	423,35	13 juin 2023	
         230613039049	5KVXER7	Exécuté	Achat	5	SHOP-C	84,72	84,72	84,99	423,60	13 juin 2023	
         230613037774	5KVXER7	Exécuté	Vente	5	SHOP-C	85,04	85,04	84,99	425,20	13 juin 2023	
         230613037666	5KVXER7	Exécuté	Achat	10	NWC-C	34,31	34,31	34,31	343,10	13 juin 2023	
         230613037079	5KVXER7	Exécuté	Achat	10	NWC-C	34,36	34,36	34,31	343,60	13 juin 2023	
         230613037045	5KVXER7	Exécuté	Vente	5	SHOP-C	84,84	84,84	84,99	424,20	13 juin 2023	
         230613036800	5KVXER7	Exécuté	Vente	5	SHOP-C	84,74	84,74	84,99	423,70	13 juin 2023	
         230613035865	5KVXER7	Exécuté	Achat	10	SHOP-C	84,42	84,42	84,99	844,20	13 juin 2023	
         230613035383	5KVXER7	Exécuté	Achat	5	SHOP-C	84,91	84,91	84,99	424,55	13 juin 2023	
         230613035256	5KVXER7	Exécuté	Achat	5	SHOP-C	84,99	84,99	84,99	424,95	13 juin 2023	
         230613035022	5KVXER7	Exécuté	Achat	200	CTS-C	3,31	3,31	3,25	662,00	13 juin 2023	
         230613034081	5KVXEY9	Exécuté	Achat	200	INO.UN-C	3,32	3,32	3,30	664,00	13 juin 2023	
         230613034040	5KVXEY9	Exécuté	Achat	5	SHOP-C	85,42	85,42	84,99	427,10	13 juin 2023	
         230613033880	5KVXEY9	Exécuté	Achat	20	NVEI-C	39,85	39,85	40,01	797,00	13 juin 2023	
         230613033082	5KVXEZ7	Exécuté	Achat	10	NVEI-C	39,88	39,84	40,01	398,40	13 juin 2023	
         230613033034	5KVXER7	Exécuté	Achat	40	NVEI-C	39,88	39,85	40,01	1 594,00	13 juin 2023	
         230613023888	5KVXER7	Exécuté	Achat	400	CTS-C	3,34	3,34	3,25	1 336,00	13 juin 2023	
         230613023641	5KVXEZ7	Exécuté	Achat	100	CTS-C	3,35	3,345	3,25	334,50	13 juin 2023	
         230613023181	5KVXER7	Exécuté	Achat	300	CTS-C	3,35	3,35	3,25	1 005,00	13 juin 2023	
         230613020690	5KVXER7	Exécuté	Vente	20	NVEI-C	40,07	40,07	40,01	801,40	13 juin 2023	
         230613019621	5KVXER7	Exécuté	Vente	20	NVEI-C	40,00	40,00	40,01	800,00	13 juin 2023	
         230613019568	5KVXEZ7	Exécuté	Achat	100	CTS-C	3,36	3,36	3,25	336,00	13 juin 2023	
         230613019422	5KVXER7	Exécuté	Achat	20	NVEI-C	39,94	39,94	40,01	798,80	13 juin 2023	
         230613019116	5KVXER7	Exécuté	Achat	40	NVEI-C	39,89	39,89	40,01	1 595,60	13 juin 2023	
         230613018943	5KVXER7	Exécuté	Achat	40	NVEI-C	39,92	39,92	40,01	1 596,80	13 juin 2023	
         230613016754	5KVXEY9	Exécuté	Achat	300	CTS-C	3,37	3,365	3,25	1 009,50	13 juin 2023	
         230613016665	5KVXER7	Exécuté	Achat	500	CTS-C	3,37	3,367	3,25	1 683,50	13 juin 2023	
         230613013129	5KVXER7	Exécuté	Vente	20	NWC-C	34,52	34,52	34,31	690,40	13 juin 2023	
         230613012978	5KVXEY9	Exécuté	Vente	20	NWC-C	34,40	34,40	34,31	688,00	13 juin 2023	
         230613012895	5KVXER7	Exécuté	Vente	40	NWC-C	34,39	34,39	34,31	1 375,60	13 juin 2023	
         230613012379	5KVXER7	Exécuté	Vente	40	NWC-C	34,37	34,37	34,31	1 374,80	13 juin 2023	
         230613012187	5KVXER7	Exécuté	Achat	200	CTS-C	3,39	3,385	3,25	677,00	13 juin 2023	
         230613012080	5KVXEZ7	Exécuté	Achat	100	CTS-C	3,39	3,385	3,25	338,50	13 juin 2023	
         230613011172	5KVXEZ7	Exécuté	Vente	10	NVEI-C	40,62	40,62	40,01	406,20	13 juin 2023	
         230613009437	5KVXEY9	Exécuté	Vente	20	HR.UN-C	10,32	10,32	10,23	206,40	13 juin 2023	
         230613009281	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,31	10,31	10,23	412,40	13 juin 2023	
         230613008038	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,28	10,28	10,23	411,20	13 juin 2023	
         230613007802	5KVXEY9	Exécuté	Vente	40	NWC-C	34,32	34,32	34,31	1 372,80	13 juin 2023	
         230613007707	5KVXER7	Exécuté	Vente	40	NWC-C	34,32	34,32	34,31	1 372,80	13 juin 2023	
         230613007141	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,34	3,34	3,30	668,00	13 juin 2023	
         230613006057	5KVXER7	Exécuté	Achat	200	CTS-C	3,41	3,405	3,25	681,00	13 juin 2023
     230612050654	5KVXEY9	Exécuté	Vente	40	HR.UN-C	10,25	10,25	10,21	410,00	12 juin 2023	
     230612050623	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,25	10,26	10,21	410,40	12 juin 2023	
     230612049963	5KVXEZ7	Exécuté	Vente	40	HR.UN-C	10,22	10,22	10,21	408,80	12 juin 2023	
     230612049904	5KVXEY9	Exécuté	Vente	40	HR.UN-C	10,22	10,22	10,21	408,80	12 juin 2023	
     230612049827	5KVXER7	Exécuté	Vente	100	HR.UN-C	10,22	10,22	10,21	1 022,00	12 juin 2023	
     230612049573	5KVXEY9	Exécuté	Achat	200	CTS-C	3,41	3,405	3,415	681,00	12 juin 2023	
     230612048733	5KVXER7	Exécuté	Achat	200	CTS-C	3,41	3,41	3,415	682,00	12 juin 2023	
     230612044755	5KVXER7	Exécuté	Vente	40	NVEI-C	40,37	40,37	40,32	1 614,80	12 juin 2023	
     230612044697	5KVXER7	Exécuté	Vente	60	NVEI-C	40,36	40,36	40,32	2 421,60	12 juin 2023	
     230612043855	5KVXER7	Exécuté	Vente	40	NVEI-C	40,23	40,23	40,32	1 609,20	12 juin 2023	
     230612043816	5KVXEZ7	Exécuté	Vente	20	NVEI-C	40,29	40,29	40,32	805,80	12 juin 2023	
     230612043746	5KVXEZ7	Exécuté	Achat	20	HR.UN-C	10,14	10,14	10,21	202,80	12 juin 2023	
     230612043708	5KVXEZ7	Exécuté	Achat	20	HR.UN-C	10,14	10,14	10,21	202,80	12 juin 2023	
     230612043418	5KVXEZ7	Exécuté	Vente	16	NVEI-C	40,21	40,21	40,32	643,36	12 juin 2023	
     230612043181	5KVXEY9	Exécuté	Achat	40	HR.UN-C	10,14	10,14	10,21	405,60	12 juin 2023	
     230612043138	5KVXER7	Exécuté	Achat	40	HR.UN-C	10,14	10,14	10,21	405,60	12 juin 2023	
     230612042046	5KVXER7	Exécuté	Achat	40	HR.UN-C	10,13	10,13	10,21	405,20	12 juin 2023	
     230612041696	5KVXER7	Exécuté	Vente	30	NVEI-C	40,21	40,21	40,32	1 206,30	12 juin 2023	
     230612041624	5KVXEY9	Exécuté	Vente	20	NVEI-C	40,20	40,20	40,32	804,00	12 juin 2023	
     230612041567	5KVXER7	Exécuté	Achat	40	HR.UN-C	10,15	10,15	10,21	406,00	12 juin 2023	
     230612041538	5KVXEY9	Exécuté	Achat	20	HR.UN-C	10,15	10,15	10,21	203,00	12 juin 2023	
     230612040994	5KVXER7	Exécuté	Achat	20	HR.UN-C	10,15	10,15	10,21	203,00	12 juin 2023	
     230612040959	5KVXER7	Exécuté	Achat	20	HR.UN-C	10,15	10,15	10,21	203,00	12 juin 2023	
     230612040934	5KVXER7	Exécuté	Vente	39	NVEI-C	40,18	40,18	40,32	1 567,02	12 juin 2023	
     230612040761	5KVXEY9	Exécuté	Vente	15	NVEI-C	40,13	40,13	40,32	601,95	12 juin 2023	
     230612040709	5KVXER7	Exécuté	Vente	5	NVEI-C	40,15	40,15	40,32	200,75	12 juin 2023	
     230612040675	5KVXER7	Exécuté	Vente	10	NVEI-C	40,13	40,13	40,32	401,30	12 juin 2023	
     230612038991	5KVXER7	Exécuté	Achat	20	HR.UN-C	10,17	10,17	10,21	203,40	12 juin 2023	
     230612035990	5KVXER7	Exécuté	Achat	40	HR.UN-C	10,18	10,18	10,21	407,20	12 juin 2023	
     230612035931	5KVXEY9	Exécuté	Achat	40	HR.UN-C	10,18	10,18	10,21	407,20	12 juin 2023	
     230612026570	5KVXEY9	Exécuté	Achat	20	NWC-C	33,97	33,97	34,21	679,40	12 juin 2023	
     230612026519	5KVXER7	Exécuté	Achat	20	NWC-C	33,98	33,98	34,21	679,60	12 juin 2023	
     230612025213	5KVXER7	Exécuté	Achat	40	NWC-C	34,05	34,05	34,21	1 362,00	12 juin 2023	
     230612025155	5KVXEY9	Exécuté	Achat	20	NWC-C	34,04	34,04	34,21	680,80	12 juin 2023	
     230612025122	5KVXER7	Exécuté	Achat	40	NWC-C	34,04	34,04	34,21	1 361,60	12 juin 2023	
     230612024214	5KVXER7	Exécuté	Vente	10	SHOP-C	85,04	85,09	86,97	850,90	12 juin 2023	
     230612023555	5KVXER7	Exécuté	Vente	10	SHOP-C	84,96	84,96	86,97	849,60	12 juin 2023	
     230612021766	5KVXEY9	Exécuté	Achat	200	CTS-C	3,41	3,405	3,415	681,00	12 juin 2023	
     230612020865	5KVXER7	Exécuté	Achat	20	SHOP-C	84,27	84,23	86,97	1 684,60	12 juin 2023	
     230612020203	5KVXER7	Exécuté	Achat	200	CTS-C	3,41	3,41	3,415	682,00	12 juin 2023	
     230612020155	5KVXER7	Exécuté	Vente	10	SHOP-C	85,02	85,02	86,97	850,20	12 juin 2023	
     230612019233	5KVXER7	Exécuté	Vente	10	SHOP-C	84,79	84,79	86,97	847,90	12 juin 2023	
     230612019151	5KVXEY9	Exécuté	Vente	10	SHOP-C	84,62	84,70	86,97	847,00	12 juin 2023	
     230612018597	5KVXER7	Exécuté	Vente	10	SHOP-C	84,50	84,50	86,97	845,00	12 juin 2023	
     230612018538	5KVXEY9	Exécuté	Vente	10	SHOP-C	84,51	84,51	86,97	845,10	12 juin 2023	
     230612018504	5KVXER7	Exécuté	Vente	20	SHOP-C	84,45	84,47	86,97	1 689,40	12 juin 2023	
     230612017880	5KVXEY9	Exécuté	Achat	10	NVEI-C	39,56	39,56	40,32	395,60	12 juin 2023	
     230612017841	5KVXER7	Exécuté	Achat	10	NVEI-C	39,56	39,56	40,32	395,60	12 juin 2023	
     230612016866	5KVXER7	Exécuté	Achat	5	NVEI-C	39,65	39,65	40,32	198,25	12 juin 2023	
     230612015918	5KVXER7	Exécuté	Achat	200	CTS-C	3,43	3,4275	3,415	685,50	12 juin 2023	
     230612013263	5KVXEY9	Exécuté	Achat	10	SHOP-C	82,89	82,87	86,97	828,70	12 juin 2023	
     230612012899	5KVXEY9	Exécuté	Vente	100	LEV-C	2,79	2,79	2,93	279,00	12 juin 2023	
     230612012869	5KVXEY9	Exécuté	Vente	200	LEV-C	2,80	2,80	2,93	560,00	12 juin 2023	
     230612012775	5KVXER7	Exécuté	Vente	300	LEV-C	2,78	2,79	2,93	837,00	12 juin 2023	
     230612012746	5KVXER7	Exécuté	Vente	200	LEV-C	2,79	2,79	2,93	558,00	12 juin 2023	
     230612012537	5KVXER7	Exécuté	Achat	10	SHOP-C	82,92	82,92	86,97	829,20	12 juin 2023	
     230612012397	5KVXEY9	Exécuté	Achat	10	SHOP-C	83,01	83,00	86,97	830,00	12 juin 2023	
     230612012130	5KVXER7	Exécuté	Achat	20	SHOP-C	83,02	83,02	86,97	1 660,40	12 juin 2023	
     230612011865	5KVXEY9	Exécuté	Achat	20	NWC-C	34,10	34,10	34,21	682,00	12 juin 2023	
     230612011796	5KVXER7	Exécuté	Achat	20	NWC-C	34,10	34,10	34,21	682,00	12 juin 2023	
     230612011729	5KVXER7	Exécuté	Achat	20	NWC-C	34,10	34,10	34,21	682,00	12 juin 2023	
     230612011495	5KVXEY9	Exécuté	Vente	200	LEV-C	2,79	2,79	2,93	558,00	12 juin 2023	
     230612011421	5KVXER7	Exécuté	Vente	400	LEV-C	2,78	2,78	2,93	1 112,00	12 juin 2023	
     230612011001	5KVXEY9	Exécuté	Vente	10	SHOP-C	83,69	83,73	86,97	837,30	12 juin 2023	
     230612010709	5KVXEY9	Exécuté	Achat	10	SHOP-C	83,31	83,31	86,97	833,10	12 juin 2023	
     230612010608	5KVXER7	Exécuté	Achat	10	SHOP-C	83,31	83,31	86,97	833,10	12 juin 2023	
     230612010007	5KVXEY9	Exécuté	Vente	10	SHOP-C	83,70	83,71	86,97	837,10	12 juin 2023	
     230612009728	5KVXEY9	Exécuté	Achat	10	SHOP-C	83,53	83,53	86,97	835,30	12 juin 2023	
     230612008620	5KVXEY9	Exécuté	Achat	70	LEV-C	2,71	2,71	2,93	189,70	12 juin 2023	
     230612008404	5KVXEY9	Exécuté	Vente	10	SHOP-C	84,53	84,53	86,97	845,30	12 juin 2023	
     230612008334	5KVXER7	Exécuté	Vente	10	SHOP-C	84,42	84,44	86,97	844,40	12 juin 2023	
     230612008122	5KVXEY9	Exécuté	Vente	10	SHOP-C	84,23	84,23	86,97	842,30	12 juin 2023	
     230612008049	5KVXER7	Exécuté	Vente	20	SHOP-C	84,20	84,25	86,97	1 685,00	12 juin 2023	
     230612006588	5KVXEY9	Exécuté	Vente	10	SHOP-C	83,98	83,98	86,97	839,80	12 juin 2023	
     230612006420	5KVXER7	Exécuté	Vente	20	SHOP-C	83,91	83,92	86,97	1 678,40	12 juin 2023	
     230612006086	5KVXEY9	Exécuté	Achat	30	LEV-C	2,69	2,68	2,93	80,40	12 juin 2023
     230609051492	5KVXEY9	Exécuté	Achat	12	NVEI-C	39,70	39,70	39,725	476,40	9 juin 2023	
     230609051437	5KVXEY9	Exécuté	Vente	200	LEV-C	2,73	2,73	2,725	546,00	9 juin 2023	
     230609051370	5KVXER7	Exécuté	Achat	2	NVEI-C	39,61	39,60	39,725	79,20	9 juin 2023	
     230609051255	5KVXER7	Exécuté	Achat	12	NVEI-C	39,63	39,63	39,725	475,56	9 juin 2023	
     230609050341	5KVXER7	Exécuté	Achat	10	NVEI-C	39,72	39,72	39,725	397,20	9 juin 2023	
     230609050282	5KVXER7	Exécuté	Vente	200	LEV-C	2,73	2,73	2,725	546,00	9 juin 2023	
     230609048252	5KVXER7	Exécuté	Achat	4	NVEI-C	39,84	39,84	39,725	159,36	9 juin 2023	
     230609048187	5KVXER7	Exécuté	Vente	200	LEV-C	2,72	2,72	2,725	544,00	9 juin 2023	
     230609048152	5KVXER7	Exécuté	Achat	13	NVEI-C	39,85	39,85	39,725	518,05	9 juin 2023	
     230609048114	5KVXER7	Exécuté	Vente	200	LEV-C	2,72	2,72	2,725	544,00	9 juin 2023	
     230609047928	5KVXEY9	Exécuté	Achat	3	NVEI-C	39,95	39,95	39,725	119,85	9 juin 2023	
     230609047883	5KVXER7	Exécuté	Achat	13	NVEI-C	39,96	39,95	39,725	519,35	9 juin 2023	
     230609044517	5KVXER7	Exécuté	Achat	20	NVEI-C	40,06	40,06	39,725	801,20	9 juin 2023	
     230609044483	5KVXER7	Exécuté	Achat	10	NVEI-C	40,09	40,08	39,725	400,80	9 juin 2023	
     230609044388	5KVXEZ7	Exécuté	Achat	4	NVEI-C	40,08	40,08	39,725	160,32	9 juin 2023	
     230609043665	5KVXEY9	Exécuté	Achat	20	NVEI-C	40,19	40,19	39,725	803,80	9 juin 2023	
     230609043638	5KVXEY9	Exécuté	Vente	8	NWC-C	34,66	34,66	34,56	277,28	9 juin 2023	
     230609043410	5KVXER7	Exécuté	Achat	5	NVEI-C	40,26	40,26	39,725	201,30	9 juin 2023	
     230609043039	5KVXEZ7	Exécuté	Achat	10	NVEI-C	40,32	40,32	39,725	403,20	9 juin 2023	
     230609042847	5KVXEZ7	Exécuté	Achat	8	NVEI-C	40,34	40,33	39,725	322,64	9 juin 2023	
     230609042305	5KVXER7	Exécuté	Achat	20	NVEI-C	40,34	40,34	39,725	806,80	9 juin 2023	
     230609042153	5KVXEZ7	Exécuté	Vente	10	NWC-C	34,60	34,60	34,56	346,00	9 juin 2023	
     230609041993	5KVXEZ7	Exécuté	Achat	4	NVEI-C	40,38	40,38	39,725	161,52	9 juin 2023	
     230609041924	5KVXEY9	Exécuté	Achat	10	NVEI-C	40,38	40,38	39,725	403,80	9 juin 2023	
     230609041840	5KVXER7	Exécuté	Achat	15	NVEI-C	40,38	40,38	39,725	605,70	9 juin 2023	
     230609033951	5KVXEY9	Exécuté	Achat	10	SHOP-C	81,38	81,38	81,44	813,80	9 juin 2023	
     230609033913	5KVXER7	Exécuté	Achat	20	SHOP-C	81,42	81,42	81,44	1 628,40	9 juin 2023	
     230609032884	5KVXER7	Exécuté	Achat	5	NVEI-C	40,52	40,52	39,725	202,60	9 juin 2023	
     230609032796	5KVXEZ7	Exécuté	Achat	5	NVEI-C	40,53	40,53	39,725	202,65	9 juin 2023	
     230609029333	5KVXEY9	Exécuté	Achat	100	CTS-C	3,45	3,45	3,45	345,00	9 juin 2023	
     230609029305	5KVXER7	Exécuté	Achat	200	CTS-C	3,45	3,45	3,45	690,00	9 juin 2023	
     230609029201	5KVXER7	Exécuté	Achat	20	SHOP-C	82,56	82,53	81,44	1 650,60	9 juin 2023	
     230609029138	5KVXEY9	Exécuté	Achat	10	SHOP-C	82,56	82,56	81,44	825,60	9 juin 2023	
     230609029062	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,34	3,34	3,34	668,00	9 juin 2023	
     230609029041	5KVXEY9	Exécuté	Achat	200	INO.UN-C	3,34	3,34	3,34	668,00	9 juin 2023	
     230609028893	5KVXER7	Exécuté	Achat	200	CTS-C	3,48	3,475	3,45	695,00	9 juin 2023	
     230609028874	5KVXEY9	Exécuté	Achat	200	CTS-C	3,48	3,475	3,45	695,00	9 juin 2023	
     230609028566	5KVXER7	Exécuté	Vente	20	SHOP-C	82,88	82,88	81,44	1 657,60	9 juin 2023	
     230609028530	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,86	82,86	81,44	828,60	9 juin 2023	
     230609028500	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,75	82,77	81,44	827,70	9 juin 2023	
     230609028465	5KVXER7	Exécuté	Vente	20	SHOP-C	82,82	82,83	81,44	1 656,60	9 juin 2023	
     230609028314	5KVXER7	Exécuté	Vente	20	SHOP-C	82,61	82,61	81,44	1 652,20	9 juin 2023	
     230609028051	5KVXER7	Exécuté	Vente	20	SHOP-C	82,55	82,58	81,44	1 651,60	9 juin 2023	
     230609027993	5KVXEY9	Exécuté	Vente	20	SHOP-C	82,50	82,50	81,44	1 650,00	9 juin 2023	
     230609022770	5KVXEZ7	Exécuté	Vente	5	NVEI-C	41,01	41,01	39,725	205,05	9 juin 2023	
     230609022657	5KVXER7	Exécuté	Achat	200	LEV-C	2,71	2,71	2,725	542,00	9 juin 2023	
     230609022110	5KVXEZ7	Exécuté	Vente	8	NVEI-C	40,95	40,95	39,725	327,60	9 juin 2023	
     230609021887	5KVXEY9	Exécuté	Achat	10	SHOP-C	80,85	80,85	81,44	808,50	9 juin 2023	
     230609021630	5KVXER7	Exécuté	Achat	400	LEV-C	2,71	2,71	2,725	1 084,00	9 juin 2023	
     230609021342	5KVXEY9	Exécuté	Achat	10	SHOP-C	81,37	81,30	81,44	813,00	9 juin 2023	
     230609021294	5KVXER7	Exécuté	Achat	20	SHOP-C	81,38	81,38	81,44	1 627,60	9 juin 2023	
     230609020811	5KVXER7	Exécuté	Vente	10	NWC-C	34,58	34,58	34,56	345,80	9 juin 2023	
     230609020507	5KVXEY9	Exécuté	Achat	200	LEV-C	2,72	2,72	2,725	544,00	9 juin 2023	
     230609020446	5KVXER7	Exécuté	Achat	200	LEV-C	2,72	2,72	2,725	544,00	9 juin 2023	
     230609020215	5KVXEY9	Exécuté	Achat	10	SHOP-C	81,54	81,48	81,44	814,80	9 juin 2023	
     230609020152	5KVXER7	Exécuté	Achat	20	SHOP-C	81,56	81,56	81,44	1 631,20	9 juin 2023	
     230609018930	5KVXER7	Exécuté	Achat	20	SHOP-C	82,04	82,04	81,44	1 640,80	9 juin 2023	
     230609018815	5KVXEY9	Exécuté	Achat	10	SHOP-C	82,15	82,15	81,44	821,50	9 juin 2023	
     230609018697	5KVXER7	Exécuté	Achat	20	SHOP-C	82,36	82,35	81,44	1 647,00	9 juin 2023	
     230609018661	5KVXEY9	Exécuté	Achat	10	SHOP-C	82,42	82,42	81,44	824,20	9 juin 2023	
     230609018326	5KVXER7	Exécuté	Vente	20	SHOP-C	82,91	82,91	81,44	1 658,20	9 juin 2023	
     230609018268	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,87	82,89	81,44	828,90	9 juin 2023	
     230609017717	5KVXER7	Exécuté	Achat	20	SHOP-C	82,46	82,46	81,44	1 649,20	9 juin 2023	
     230609017243	5KVXER7	Exécuté	Vente	20	SHOP-C	82,63	82,63	81,44	1 652,60	9 juin 2023	
     230609016989	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,34	82,34	81,44	823,40	9 juin 2023	
     230609016935	5KVXER7	Exécuté	Vente	20	SHOP-C	82,35	82,35	81,44	1 647,00	9 juin 2023	
     230609016792	5KVXER7	Exécuté	Achat	200	LEV-C	2,72	2,72	2,725	544,00	9 juin 2023	
     230609016639	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,26	82,26	81,44	822,60	9 juin 2023	
     230609016535	5KVXEY9	Exécuté	Vente	10	NVEI-C	41,03	41,03	39,725	410,30	9 juin 2023	
     230609016048	5KVXER7	Exécuté	Vente	5	NVEI-C	40,89	40,89	39,725	204,45	9 juin 2023	
     230609015714	5KVXER7	Exécuté	Vente	20	SHOP-C	82,24	82,24	81,44	1 644,80	9 juin 2023	
     230609015588	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,14	82,14	81,44	821,40	9 juin 2023	
     230609015512	5KVXER7	Exécuté	Vente	20	SHOP-C	82,14	82,14	81,44	1 642,80	9 juin 2023	
     230609014879	5KVXEY9	Exécuté	Vente	200	CTS-C	3,51	3,51	3,45	702,00	9 juin 2023	
     230609014832	5KVXEY9	Exécuté	Vente	200	CTS-C	3,52	3,52	3,45	704,00	9 juin 2023	
     230609014774	5KVXER7	Exécuté	Vente	300	CTS-C	3,51	3,51167	3,45	1 053,5001	9 juin 2023	
     230609014706	5KVXER7	Exécuté	Vente	200	CTS-C	3,52	3,52	3,45	704,00	9 juin 2023	
     230609014590	5KVXER7	Exécuté	Vente	400	CTS-C	3,51	3,51	3,45	1 404,00	9 juin 2023	
     230609011436	5KVXEY9	Exécuté	Vente	170	CTS-C	3,51	3,51	3,45	596,70	9 juin 2023	
     230609011364	5KVXER7	Exécuté	Vente	200	CTS-C	3,51	3,51	3,45	702,00	9 juin 2023	
     230609007510	5KVXEY9	Exécuté	Achat	8	NWC-C	34,38	34,38	34,56	275,04	9 juin 2023	
     230609007220	5KVXEZ7	Exécuté	Achat	10	NWC-C	34,44	34,44	34,56	344,40	9 juin 2023	
     230609007104	5KVXER7	Exécuté	Achat	10	NWC-C	34,44	34,44	34,56	344,40	9 juin 2023	
     230609005757	5KVXEY9	Exécuté	Achat	100	LEV-C	2,72	2,72	2,725	272,00	9 juin 2023	
     230609005661	5KVXER7	Exécuté	Achat	100	LEV-C	2,72	2,72	2,725	272,00	9 juin 2023
         230608049900	5KVXEZ7	Exécuté	Achat	8	NVEI-C	40,67	40,67	40,80	325,36	8 juin 2023	
         230608049833	5KVXEY9	Exécuté	Achat	20	NVEI-C	40,67	40,67	40,80	813,40	8 juin 2023	
         230608047906	5KVXEY9	Exécuté	Achat	200	LEV-C	2,75	2,75	2,75	550,00	8 juin 2023	
         230608047006	5KVXEZ7	Exécuté	Achat	5	NVEI-C	40,90	40,90	40,80	204,50	8 juin 2023	
         230608046863	5KVXEY9	Exécuté	Achat	10	NVEI-C	40,93	40,93	40,80	409,30	8 juin 2023	
         230608045790	5KVXEY9	Exécuté	Vente	10	LSPD-C	19,69	19,69	19,73	196,90	8 juin 2023	
         230608041874	5KVXEY9	Exécuté	Achat	5	NVEI-C	41,00	41,00	40,80	205,00	8 juin 2023	
         230608041044	5KVXER7	Exécuté	Achat	200	LEV-C	2,76	2,755	2,75	551,00	8 juin 2023	
         230608037917	5KVXEZ7	Exécuté	Vente	10	NWC-C	35,06	35,06	35,02	350,60	8 juin 2023	
         230608037883	5KVXEY9	Exécuté	Vente	20	NWC-C	35,06	35,06	35,02	701,20	8 juin 2023	
         230608037819	5KVXEY9	Exécuté	Vente	20	NWC-C	35,04	35,04	35,02	700,80	8 juin 2023	
         230608037770	5KVXER7	Exécuté	Vente	10	NWC-C	35,05	35,06	35,02	350,60	8 juin 2023	
         230608030261	5KVXEY9	Exécuté	Achat	100	LEV-C	2,77	2,77	2,75	277,00	8 juin 2023	
         230608030146	5KVXER7	Exécuté	Achat	100	LEV-C	2,77	2,77	2,75	277,00	8 juin 2023	
         230608022662	5KVXEY9	Exécuté	Achat	9	LSPD-C	19,62	19,62	19,73	176,58	8 juin 2023	
         230608020821	5KVXEZ7	Exécuté	Vente	25	NWC-C	34,98	34,98	35,02	874,50	8 juin 2023	
         230608019961	5KVXER7	Exécuté	Achat	10	NWC-C	34,57	34,57	35,02	345,70	8 juin 2023	
         230608019701	5KVXEY9	Exécuté	Achat	20	NWC-C	34,57	34,57	35,02	691,40	8 juin 2023	
         230608019397	5KVXEY9	Exécuté	Vente	20	NWC-C	35,01	35,01	35,02	700,20	8 juin 2023	
         230608019323	5KVXER7	Exécuté	Vente	10	NWC-C	34,98	34,98	35,02	349,80	8 juin 2023	
         230608016235	5KVXER7	Exécuté	Achat	200	CTS-C	3,39	3,385	3,425	677,00	8 juin 2023	
         230608015655	5KVXER7	Exécuté	Vente	20	NWC-C	34,94	34,94	35,02	698,80	8 juin 2023	
         230608015378	5KVXEY9	Exécuté	Vente	40	NWC-C	34,94	34,94	35,02	1 397,60	8 juin 2023	
         230608015238	5KVXER7	Exécuté	Vente	30	NWC-C	34,88	34,88	35,02	1 046,40	8 juin 2023	
         230608014551	5KVXEY9	Exécuté	Achat	70	CTS-C	3,41	3,41	3,425	238,70	8 juin 2023	
         230608013683	5KVXEY9	Exécuté	Achat	100	CTS-C	3,41	3,405	3,425	340,50	8 juin 2023	
         230608010910	5KVXER7	Exécuté	Achat	5	NVEI-C	40,51	40,51	40,80	202,55	8 juin 2023	
         230608010793	5KVXEY9	Exécuté	Achat	10	NVEI-C	40,51	40,51	40,80	405,10	8 juin 2023	
         230608010731	5KVXEY9	Exécuté	Achat	1	LSPD-C	19,62	19,62	19,73	19,62	8 juin 2023	
         230608009931	5KVXEY9	Exécuté	Achat	200	CTS-C	3,41	3,405	3,425	681,00	8 juin 2023	
         230608009097	5KVXER7	Exécuté	Achat	200	CTS-C	3,41	3,41	3,425	682,00	8 juin 2023	
         230608008984	5KVXEY9	Exécuté	Achat	20	NWC-C	34,45	34,45	35,02	689,00	8 juin 2023	
         230608008269	5KVXER7	Exécuté	Achat	20	NWC-C	34,48	34,48	35,02	689,60	8 juin 2023	
         230608007718	5KVXER7	Exécuté	Achat	200	CTS-C	3,43	3,425	3,425	685,00	8 juin 2023	
         230608007663	5KVXER7	Exécuté	Vente	20	NWC-C	34,83	34,86	35,02	697,20	8 juin 2023	
         230608005494	5KVXEY9	Exécuté	Vente	40	NWC-C	34,86	34,86	35,02	1 394,40	8 juin 2023	
         230608005170	5KVXEY9	Exécuté	Vente	24	NWC-C	34,38	34,38	35,02	825,12	8 juin 2023	
         230608004348	5KVXER7	Exécuté	Vente	21	NWC-C	34,45	34,48	35,02	724,08	8 juin 2023       
         230607056005	5KVXEY9	Exécuté	Vente	2	SHOP-C	79,59	79,59	80,47	159,18	7 juin 2023	
         230607055895	5KVXER7	Exécuté	Vente	2	SHOP-C	79,59	79,59	80,47	159,18	7 juin 2023	
         230607055485	5KVXER7	Exécuté	Vente	200	FOOD-C	0,49	0,49	0,49	98,00	7 juin 2023	
         230607055446	5KVXER7	Exécuté	Achat	2	SHOP-C	79,40	79,40	80,47	158,80	7 juin 2023	
         230607055399	5KVXEY9	Exécuté	Achat	2	SHOP-C	79,40	79,37	80,47	158,74	7 juin 2023	
         230607055360	5KVXEY9	Exécuté	Vente	300	FOOD-C	0,49	0,49	0,49	147,00	7 juin 2023	
         230607055323	5KVXER7	Exécuté	Vente	400	FOOD-C	0,49	0,49	0,49	196,00	7 juin 2023	
         230607054106	5KVXER7	Exécuté	Vente	200	CTS-C	3,49	3,49	3,475	698,00	7 juin 2023	
         230607048393	5KVXEZ7	Exécuté	Achat	6	NWC-C	33,76	33,76	33,65	202,56	7 juin 2023	
         230607048217	5KVXEZ7	Exécuté	Vente	5	NVEI-C	41,30	41,30	41,28	206,50	7 juin 2023	
         230607044666	5KVXEY9	Exécuté	Achat	4	NWC-C	34,04	34,04	33,65	136,16	7 juin 2023	
         230607044293	5KVXEY9	Exécuté	Achat	100	INO.UN-C	3,33	3,33	3,34	333,00	7 juin 2023	
         230607043916	5KVXEY9	Exécuté	Achat	20	NWC-C	34,04	34,04	33,65	680,80	7 juin 2023	
         230607043880	5KVXER7	Exécuté	Achat	6	NWC-C	34,04	34,04	33,65	204,24	7 juin 2023	
         230607043623	5KVXER7	Exécuté	Achat	15	NWC-C	34,10	34,09	33,65	511,35	7 juin 2023	
         230607043571	5KVXEY9	Exécuté	Vente	200	CTS-C	3,52	3,52	3,475	704,00	7 juin 2023	
         230607043499	5KVXER7	Exécuté	Vente	200	CTS-C	3,52	3,52	3,475	704,00	7 juin 2023	
         230607043452	5KVXEY9	Exécuté	Achat	20	NWC-C	34,07	34,07	33,65	681,40	7 juin 2023	
         230607043204	5KVXEY9	Exécuté	Achat	26	NWC-C	34,16	34,16	33,65	888,16	7 juin 2023	
         230607042211	5KVXER7	Exécuté	Achat	2	NWC-C	34,21	34,19	33,65	68,38	7 juin 2023	
         230607042179	5KVXEY9	Exécuté	Achat	4	NWC-C	34,21	34,20	33,65	136,80	7 juin 2023	
         230607042140	5KVXER7	Exécuté	Vente	1	SHOP-C	81,18	81,18	80,47	81,18	7 juin 2023	
         230607042115	5KVXEY9	Exécuté	Vente	2	SHOP-C	81,19	81,19	80,47	162,38	7 juin 2023	
         230607041727	5KVXER7	Exécuté	Achat	20	NWC-C	34,29	34,27	33,65	685,40	7 juin 2023	
         230607040695	5KVXER7	Exécuté	Achat	1	SHOP-C	80,76	80,76	80,47	80,76	7 juin 2023	
         230607040658	5KVXEY9	Exécuté	Achat	2	SHOP-C	80,75	80,75	80,47	161,50	7 juin 2023	
         230607040531	5KVXER7	Exécuté	Achat	28	NWC-C	34,26	34,26	33,65	959,28	7 juin 2023	
         230607040498	5KVXEY9	Exécuté	Achat	25	NWC-C	34,35	34,27	33,65	856,75	7 juin 2023	
         230607040442	5KVXEY9	Exécuté	Vente	300	INO.UN-C	3,36	3,36	3,34	1 008,00	7 juin 2023	
         230607040398	5KVXER7	Exécuté	Vente	300	INO.UN-C	3,35	3,36	3,34	1 008,00	7 juin 2023	
         230607036202	5KVXEZ7	Exécuté	Achat	9	NWC-C	34,60	34,60	33,65	311,40	7 juin 2023	
         230607035796	5KVXER7	Exécuté	Vente	20	NWC-C	34,34	34,34	33,65	686,80	7 juin 2023	
         230607035567	5KVXEZ7	Exécuté	Achat	20	NWC-C	34,68	34,68	33,65	693,60	7 juin 2023	
         230607035516	5KVXEY9	Exécuté	Achat	5	NWC-C	34,70	34,70	33,65	173,50	7 juin 2023	
         230607035439	5KVXER7	Exécuté	Achat	10	NWC-C	34,70	34,70	33,65	347,00	7 juin 2023	
         230607035397	5KVXEY9	Exécuté	Achat	20	NWC-C	34,73	34,70	33,65	694,00	7 juin 2023	
         230607035332	5KVXER7	Exécuté	Achat	20	NWC-C	34,73	34,73	33,65	694,60	7 juin 2023	
         230607034312	5KVXER7	Exécuté	Achat	200	CTS-C	3,49	3,49	3,475	698,00	7 juin 2023	
         230607034262	5KVXER7	Exécuté	Achat	200	CTS-C	3,50	3,49	3,475	698,00	7 juin 2023	
         230607034213	5KVXEY9	Exécuté	Achat	200	CTS-C	3,51	3,51	3,475	702,00	7 juin 2023	
         230607033951	5KVXEY9	Exécuté	Achat	10	SHOP-C	81,86	81,86	80,47	818,60	7 juin 2023	
         230607033917	5KVXER7	Exécuté	Achat	20	SHOP-C	81,89	81,88	80,47	1 637,60	7 juin 2023	
         230607032426	5KVXEZ7	Exécuté	Achat	10	NVEI-C	41,33	41,33	41,28	413,30	7 juin 2023	
         230607032162	5KVXER7	Exécuté	Achat	20	NVEI-C	41,34	41,34	41,28	826,80	7 juin 2023	
         230607032127	5KVXER7	Exécuté	Achat	20	NVEI-C	41,38	41,38	41,28	827,60	7 juin 2023	
         230607032037	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,35	82,35	80,47	823,50	7 juin 2023	
         230607031976	5KVXER7	Exécuté	Achat	200	CTS-C	3,53	3,525	3,475	705,00	7 juin 2023	
         230607031253	5KVXER7	Exécuté	Achat	200	CTS-C	3,53	3,525	3,475	705,00	4 août 2023	
         230607031210	5KVXEY9	Exécuté	Achat	200	CTS-C	3,53	3,53	3,475	706,00	7 juin 2023	
         230607030755	5KVXER7	Exécuté	Achat	20	SHOP-C	81,96	81,96	80,47	1 639,20	7 juin 2023	
         230607030703	5KVXEY9	Exécuté	Achat	40	SHOP-C	82,06	82,05	80,47	3 282,00	7 juin 2023	
         230607030654	5KVXER7	Exécuté	Achat	40	SHOP-C	82,15	82,12	80,47	3 284,80	7 juin 2023	
         230607030365	5KVXEY9	Exécuté	Achat	240	INO.UN-C	3,35	3,35	3,34	804,00	7 juin 2023	
         230607030306	5KVXER7	Exécuté	Achat	300	INO.UN-C	3,35	3,35	3,34	1 005,00	7 juin 2023	
         230607030282	5KVXEY9	Exécuté	Vente	20	SHOP-C	82,41	82,41	80,47	1 648,20	7 juin 2023	
         230607030213	5KVXER7	Exécuté	Achat	20	SHOP-C	82,17	82,17	80,47	1 643,40	7 juin 2023	
         230607030159	5KVXER7	Exécuté	Vente	20	SHOP-C	82,35	82,35	80,47	1 647,00	7 juin 2023	
         230607029941	5KVXEY9	Exécuté	Achat	20	SHOP-C	82,14	82,14	80,47	1 642,80	7 juin 2023	
         230607029842	5KVXER7	Exécuté	Achat	20	SHOP-C	82,15	82,15	80,47	1 643,00	7 juin 2023	
         230607027981	5KVXER7	Exécuté	Vente	20	NVEI-C	41,61	41,61	41,28	832,20	7 juin 2023	
         230607025601	5KVXER7	Exécuté	Vente	20	NVEI-C	41,58	41,58	41,28	831,60	7 juin 2023
 
         230606046147	5KVXEZ7	Exécuté	Vente	5	NVEI-C	41,48	41,48	41,52	207,40	6 juin 2023	
         230606045274	5KVXEY9	Exécuté	Vente	100	LEV-C	2,82	2,82	2,79	282,00	6 juin 2023	
         230606045179	5KVXER7	Exécuté	Vente	300	LEV-C	2,82	2,82	2,79	846,00	6 juin 2023	
         230606044955	5KVXEY9	Exécuté	Vente	200	LEV-C	2,82	2,82	2,79	564,00	6 juin 2023	
         230606044921	5KVXER7	Exécuté	Vente	500	LEV-C	2,81	2,812	2,79	1 406,00	6 juin 2023	
         230606044535	5KVXER7	Exécuté	Vente	500	LEV-C	2,79	2,792	2,79	1 396,00	6 juin 2023	
         230606042912	5KVXEY9	Exécuté	Vente	265	LEV-C	2,76	2,76	2,79	731,40	6 juin 2023	
         230606042812	5KVXEZ7	Exécuté	Vente	140	LEV-C	2,77	2,77	2,79	387,80	6 juin 2023	
         230606042745	5KVXER7	Exécuté	Vente	500	LEV-C	2,76	2,761	2,79	1 380,50	6 juin 2023	
         230606035350	5KVXER7	Exécuté	Vente	10	SHOP-C	84,74	84,76	85,35	847,60	6 juin 2023	
         230606033206	5KVXER7	Exécuté	Vente	10	SHOP-C	83,84	83,84	85,35	838,40	6 juin 2023	
         230606030642	5KVXER7	Exécuté	Achat	10	SHOP-C	83,55	83,45	85,35	834,50	6 juin 2023	
         230606024074	5KVXEY9	Exécuté	Vente	100	CTS-C	3,60	3,60	3,58	360,00	6 juin 2023	
         230606023901	5KVXEY9	Exécuté	Vente	200	CTS-C	3,61	3,61	3,58	722,00	6 juin 2023	
         230606023865	5KVXER7	Exécuté	Vente	100	CTS-C	3,61	3,61	3,58	361,00	6 juin 2023	
         230606023809	5KVXER7	Exécuté	Vente	200	CTS-C	3,59	3,595	3,58	719,00	6 juin 2023	
         230606023521	5KVXEY9	Exécuté	Vente	150	CTS-C	3,57	3,58	3,58	537,00	6 juin 2023	
         230606023477	5KVXER7	Exécuté	Vente	300	CTS-C	3,56	3,57	3,58	1 071,00	6 juin 2023	
         230606023424	5KVXER7	Exécuté	Vente	200	CTS-C	3,55	3,555	3,58	711,00	6 juin 2023	
         230606022540	5KVXEY9	Exécuté	Vente	100	LEV-C	2,58	2,58	2,79	258,00	6 juin 2023	
         230606022506	5KVXER7	Exécuté	Vente	200	LEV-C	2,58	2,58	2,79	516,00	6 juin 2023	
         230606022144	5KVXEZ7	Exécuté	Vente	160	LEV-C	2,58	2,58	2,79	412,80	6 juin 2023	
         230606014143	5KVXER7	Exécuté	Vente	10	SHOP-C	84,16	84,18	85,35	841,80	6 juin 2023	
         230606014102	5KVXEY9	Exécuté	Vente	10	SHOP-C	84,24	84,24	85,35	842,40	6 juin 2023	
         230606013838	5KVXER7	Exécuté	Vente	10	SHOP-C	84,13	84,13	85,35	841,30	6 juin 2023	
         230606012680	5KVXER7	Exécuté	Achat	10	SHOP-C	83,51	83,51	85,35	835,10	6 juin 2023	
         230606008647	5KVXER7	Exécuté	Vente	100	CTS-C	3,44	3,44	3,58	344,00	6 juin 2023	
         230606006653	5KVXEY9	Exécuté	Vente	10	SHOP-C	83,62	83,62	85,35	836,20	6 juin 2023	
         230606006573	5KVXER7	Exécuté	Vente	10	SHOP-C	83,63	83,63	85,35	836,30	6 juin 2023	
         230606005750	5KVXER7	Exécuté	Vente	10	SHOP-C	82,75	82,75	85,35	827,50	6 juin 2023	
         230606005344	5KVXEY9	Exécuté	Vente	10	SHOP-C	82,75	82,75	85,35	827,50	6 juin 2023	
         230606004840	5KVXER7	Exécuté	Achat	100	CTS-C	3,40	3,395	3,58	339,50	6 juin 2023
         230605047691	5KVXER7	Exécuté	Achat	15	NVEI-C	40,83	40,83	40,81	612,45	5 juin 2023	
         230605047141	5KVXEY9	Exécuté	Achat	50	CTS-C	3,45	3,45	3,47	172,50	5 juin 2023	
         230605046940	5KVXER7	Exécuté	Achat	300	CTS-C	3,45	3,45	3,47	1 035,00	5 juin 2023	
         230605046880	5KVXEZ7	Exécuté	Achat	140	LEV-C	2,51	2,51	2,52	351,40	5 juin 2023	
         230605046630	5KVXEY9	Exécuté	Achat	5	NVEI-C	40,92	40,92	40,81	204,60	5 juin 2023	
         230605045956	5KVXER7	Exécuté	Achat	200	CTS-C	3,46	3,455	3,47	691,00	5 juin 2023	
         230605044643	5KVXER7	Exécuté	Achat	12	NVEI-C	41,03	41,03	40,81	492,36	5 juin 2023	
         230605044247	5KVXEY9	Exécuté	Vente	200	FOOD-C	0,52	0,53	0,51	106,00	5 juin 2023	
         230605044222	5KVXER7	Exécuté	Vente	400	FOOD-C	0,52	0,53	0,51	212,00	5 juin 2023	
         230605044060	5KVXEZ7	Exécuté	Achat	5	NVEI-C	41,05	41,05	40,81	205,25	5 juin 2023	
         230605043575	5KVXER7	Exécuté	Vente	20	SHOP-C	80,49	80,49	80,56	1 609,80	5 juin 2023	
         230605043294	5KVXEZ7	Exécuté	Achat	10	NVEI-C	41,05	41,05	40,81	410,50	5 juin 2023	
         230605042236	5KVXEZ7	Exécuté	Vente	5	SHOP-C	80,42	80,42	80,56	402,10	5 juin 2023	
         230605039989	5KVXER7	Exécuté	Achat	20	SHOP-C	80,08	80,07	80,56	1 601,40	5 juin 2023	
         230605039880	5KVXEZ7	Exécuté	Achat	5	SHOP-C	80,18	80,17	80,56	400,85	5 juin 2023	
         230605039854	5KVXEY9	Exécuté	Achat	100	CTS-C	3,48	3,475	3,47	347,50	5 juin 2023	
         230605039833	5KVXER7	Exécuté	Achat	100	CTS-C	3,47	3,47	3,47	347,00	5 juin 2023	
         230605039636	5KVXER7	Exécuté	Vente	200	FOOD-C	0,5000	0,5000	0,51	100,00	5 juin 2023	
         230605039613	5KVXEZ7	Exécuté	Vente	5	SHOP-C	80,39	80,39	80,56	401,95	5 juin 2023	
         230605039227	5KVXEY9	Exécuté	Vente	100	FOOD-C	0,5000	0,5000	0,51	50,00	5 juin 2023	
         230605038139	5KVXEY9	Exécuté	Achat	100	CTS-C	3,48	3,48	3,47	348,00	5 juin 2023	
         230605038107	5KVXER7	Exécuté	Achat	200	CTS-C	3,49	3,49	3,47	698,00	5 juin 2023	
         230605038021	5KVXER7	Exécuté	Vente	20	SHOP-C	80,55	80,55	80,56	1 611,00	5 juin 2023	
         230605037633	5KVXEY9	Exécuté	Achat	100	CTS-C	3,51	3,505	3,47	350,50	5 juin 2023	
         230605037619	5KVXEY9	Exécuté	Vente	20	SHOP-C	80,56	80,59	80,56	1 611,80	5 juin 2023	
         230605036931	5KVXEY9	Exécuté	Achat	100	CTS-C	3,51	3,505	3,47	350,50	5 juin 2023	
         230605036804	5KVXEZ7	Exécuté	Achat	5	SHOP-C	80,07	80,06	80,56	400,30	5 juin 2023	
         230605036765	5KVXER7	Exécuté	Achat	20	SHOP-C	80,13	80,13	80,56	1 602,60	5 juin 2023	
         230605036743	5KVXEY9	Exécuté	Achat	10	SHOP-C	80,11	80,11	80,56	801,10	5 juin 2023	
         230605036665	5KVXER7	Exécuté	Achat	20	SHOP-C	80,15	80,12	80,56	1 602,40	5 juin 2023	
         230605036603	5KVXEY9	Exécuté	Achat	20	SHOP-C	80,12	80,12	80,56	1 602,40	5 juin 2023	
         230605036556	5KVXEY9	Exécuté	Achat	20	SHOP-C	80,21	80,21	80,56	1 604,20	5 juin 2023	
         230605036207	5KVXER7	Exécuté	Achat	100	CTS-C	3,51	3,505	3,47	350,50	5 juin 2023	
         230605036142	5KVXER7	Exécuté	Achat	100	CTS-C	3,52	3,52	3,47	352,00	5 juin 2023	
         230605036038	5KVXEZ7	Exécuté	Vente	10	NVEI-C	41,53	41,53	40,81	415,30	5 juin 2023	
         230605035977	5KVXEY9	Exécuté	Vente	20	NVEI-C	41,52	41,52	40,81	830,40	5 juin 2023	
         230605035910	5KVXEY9	Exécuté	Vente	20	NVEI-C	41,45	41,45	40,81	829,00	5 juin 2023	
         230605035757	5KVXER7	Exécuté	Achat	10	SHOP-C	80,31	80,21	80,56	802,10	5 juin 2023	
         230605035720	5KVXER7	Exécuté	Achat	10	SHOP-C	80,38	80,34	80,56	803,40	5 juin 2023	
         230605033618	5KVXEZ7	Exécuté	Vente	2	SHOP-C	81,65	81,66	80,56	163,32	5 juin 2023	
         230605031927	5KVXEZ7	Exécuté	Vente	3	SHOP-C	81,64	81,64	80,56	244,92	5 juin 2023	
         230605031335	5KVXEZ7	Exécuté	Vente	3	SHOP-C	81,35	81,35	80,56	244,05	5 juin 2023	
         230605031305	5KVXEY9	Exécuté	Vente	10	SHOP-C	81,33	81,33	80,56	813,30	5 juin 2023	
         230605031266	5KVXER7	Exécuté	Vente	10	SHOP-C	81,34	81,34	80,56	813,40	5 juin 2023	
         230605031184	5KVXER7	Exécuté	Vente	10	SHOP-C	81,28	81,28	80,56	812,80	5 juin 2023	
         230605030809	5KVXEY9	Exécuté	Achat	200	INO.UN-C	3,36	3,36	3,32	672,00	5 juin 2023	
         230605030750	5KVXEY9	Exécuté	Vente	10	SHOP-C	81,24	81,24	80,56	812,40	5 juin 2023	
         230605030722	5KVXER7	Exécuté	Vente	20	SHOP-C	81,25	81,25	80,56	1 625,00	5 juin 2023	
         230605030666	5KVXER7	Exécuté	Vente	20	SHOP-C	81,21	81,21	80,56	1 624,20	5 juin 2023	
         230605030611	5KVXER7	Exécuté	Vente	10	SHOP-C	81,23	81,23	80,56	812,30	5 juin 2023	
         230605030582	5KVXEY9	Exécuté	Vente	10	SHOP-C	81,20	81,20	80,56	812,00	5 juin 2023	
         230605029849	5KVXEY9	Exécuté	Vente	6	SHOP-C	81,09	81,09	80,56	486,54	5 juin 2023	
         230605029778	5KVXEZ7	Exécuté	Vente	1	SHOP-C	81,11	81,11	80,56	81,11	5 juin 2023	
         230605025922	5KVXER7	Exécuté	Vente	5	SHOP-C	79,84	79,84	80,56	399,20	5 juin 2023	
         230605025635	5KVXEY9	Exécuté	Achat	100	INO.UN-C	3,37	3,37	3,32	337,00	5 juin 2023	
         230605025547	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,37	3,37	3,32	674,00	5 juin 2023	
         230605024681	5KVXER7	Exécuté	Vente	10	SHOP-C	79,87	79,93	80,56	799,30	5 juin 2023	
         230605022531	5KVXER7	Exécuté	Vente	10	SHOP-C	79,49	79,51	80,56	795,10	5 juin 2023	
         230605022504	5KVXEY9	Exécuté	Vente	10	SHOP-C	79,56	79,56	80,56	795,60	5 juin 2023	
         230605022459	5KVXER7	Exécuté	Vente	20	SHOP-C	79,48	79,53	80,56	1 590,60	5 juin 2023	
         230605018332	5KVXEZ7	Exécuté	Achat	20	LEV-C	2,51	2,51	2,52	50,20	5 juin 2023	
         230605017838	5KVXER7	Exécuté	Achat	200	LEV-C	2,51	2,51	2,52	502,00	5 juin 2023	
         230605017797	5KVXEY9	Exécuté	Achat	100	LEV-C	2,51	2,51	2,52	251,00	5 juin 2023	
         230417049918	5KVXER7	Exécuté	Vente	100	FOOD-C	0,49	0,49	0,51	49,00	5 juin 2023	
         230417049884	5KVXER7	Exécuté	Vente	200	FOOD-C	0,49	0,49	0,51	98,00	5 juin 2023       
         230602051669	5KVXEZ7	Exécuté	Achat	7	NVEI-C	40,77	40,77	285,39	2 juin 2023	
         230602051594	5KVXEY9	Exécuté	Achat	7	NVEI-C	40,77	40,77	285,39	2 juin 2023	
         230602051556	5KVXER7	Exécuté	Achat	5	NVEI-C	40,76	40,76	203,80	2 juin 2023	
         230602051478	5KVXEZ7	Exécuté	Achat	10	NVEI-C	40,85	40,84	408,40	2 juin 2023	
         230602051446	5KVXER7	Exécuté	Achat	10	NVEI-C	40,88	40,87	408,70	2 juin 2023	
         230602051426	5KVXEY9	Exécuté	Achat	10	NVEI-C	40,88	40,87	408,70	2 juin 2023	
         230602048322	5KVXEY9	Exécuté	Achat	10	NVEI-C	40,88	40,88	408,80	2 juin 2023	
         230602048291	5KVXER7	Exécuté	Achat	13	NVEI-C	40,90	40,90	531,70	2 juin 2023	
         230602048262	5KVXER7	Exécuté	Vente	100	INO.UN-C	3,41	3,41	341,00	2 juin 2023	
         230602048241	5KVXER7	Exécuté	Vente	100	INO.UN-C	3,41	3,415	341,50	2 juin 2023	
         230602032036	5KVXEY9	Exécuté	Achat	5	SHOP-C	78,46	78,46	392,30	2 juin 2023	
         230602031958	5KVXEY9	Exécuté	Vente	15	NVEI-C	40,91	40,91	613,65	2 juin 2023	
         230602031920	5KVXER7	Exécuté	Achat	5	SHOP-C	78,58	78,58	392,90	2 juin 2023	
         230602031858	5KVXER7	Exécuté	Achat	5	SHOP-C	78,59	78,59	392,95	2 juin 2023	
         230602031785	5KVXER7	Exécuté	Vente	20	NVEI-C	40,82	40,82	816,40	2 juin 2023	
         230602030335	5KVXER7	Exécuté	Achat	15	NVEI-C	40,69	40,69	610,35	2 juin 2023	
         230602030303	5KVXEY9	Exécuté	Achat	15	NVEI-C	40,69	40,69	610,35	2 juin 2023	
         230602023819	5KVXEY9	Exécuté	Achat	20	NVEI-C	40,78	40,78	815,60	2 juin 2023	
         230602023718	5KVXER7	Exécuté	Achat	20	NVEI-C	40,75	40,75	815,00	2 juin 2023	
         230602022251	5KVXEY9	Exécuté	Achat	10	NVEI-C	40,82	40,82	408,20	2 juin 2023	
         230602022228	5KVXER7	Exécuté	Achat	10	NVEI-C	40,89	40,89	408,90	2 juin 2023	
         230602019197	5KVXEY9	Exécuté	Achat	180	FOOD-C	0,47	0,47	84,60	2 juin 2023	
         230602019131	5KVXER7	Exécuté	Achat	500	FOOD-C	0,47	0,47	235,00	2 juin 2023	
         230602018859	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,39	10,40	416,00	2 juin 2023	
         230602018378	5KVXER7	Exécuté	Achat	40	NVEI-C	40,95	40,95	1 638,00	2 juin 2023	
         230602018312	5KVXEZ7	Exécuté	Achat	5	NVEI-C	41,02	41,02	205,10	2 juin 2023	
         230602018241	5KVXEY9	Exécuté	Achat	19	NVEI-C	41,03	41,03	779,57	2 juin 2023	
         230602010043	5KVXER7	Exécuté	Achat	40	NVEI-C	41,32	41,32	1 652,80	2 juin 2023	
         230602009873	5KVXEY9	Exécuté	Achat	20	NVEI-C	41,41	41,41	828,20	2 juin 2023	
         230602009641	5KVXEY9	Exécuté	Vente	100	LEV-C	2,56	2,56	256,00	2 juin 2023	
         230602009592	5KVXER7	Exécuté	Vente	200	LEV-C	2,56	2,56	512,00	2 juin 2023	
         230602009260	5KVXEY9	Exécuté	Achat	10	NVEI-C	41,42	41,42	414,20	2 juin 2023	
         230602009070	5KVXER7	Exécuté	Achat	40	NVEI-C	41,58	41,58	1 663,20	2 juin 2023	
         230602006008	5KVXER7	Exécuté	Achat	10	NVEI-C	41,56	41,56	415,60	2 juin 2023
         230601046869	5KVXER7	Exécuté	Vente	20	LSPD-C	19,88	19,88	19,81	397,60	1 juin 2023	
         230601046834	5KVXEZ7	Exécuté	Vente	10	LSPD-C	19,87	19,87	19,81	198,70	1 juin 2023	
         230601046773	5KVXEY9	Exécuté	Vente	20	LSPD-C	19,88	19,88	19,81	397,60	1 juin 2023	
         230601046722	5KVXER7	Exécuté	Vente	40	LSPD-C	19,87	19,87	19,81	794,80	1 juin 2023	
         230601046645	5KVXER7	Exécuté	Vente	40	LSPD-C	19,81	19,81	19,81	792,40	1 juin 2023	
         230601046466	5KVXEZ7	Exécuté	Vente	20	LSPD-C	19,82	19,82	19,81	396,40	1 juin 2023	
         230601046314	5KVXEY9	Exécuté	Vente	20	LSPD-C	19,81	19,81	19,81	396,20	1 juin 2023	
         230601046249	5KVXEY9	Exécuté	Vente	30	HR.UN-C	10,26	10,26	10,30	307,80	1 juin 2023	
         230601046208	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,26	10,26	10,30	410,40	1 juin 2023	
         230601044512	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,37	3,37	3,36	674,00	1 juin 2023	
         230601040885	5KVXEY9	Exécuté	Vente	20	LSPD-C	19,56	19,56	19,81	391,20	1 juin 2023	
         230601038388	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,21	10,21	10,30	408,40	1 juin 2023	
         230601033762	5KVXER7	Exécuté	Achat	10	NVEI-C	41,98	41,98	41,80	419,80	1 juin 2023	
         230601033701	5KVXER7	Exécuté	Vente	40	LSPD-C	19,45	19,45	19,81	778,00	1 juin 2023	
         230601032028	5KVXEY9	Exécuté	Vente	20	LSPD-C	19,44	19,44	19,81	388,80	1 juin 2023	
         230601031986	5KVXER7	Exécuté	Vente	40	LSPD-C	19,43	19,43	19,81	777,20	1 juin 2023	
         230601031609	5KVXER7	Exécuté	Vente	10	SHOP-C	78,64	78,64	78,10	786,40	1 juin 2023	
         230601030596	5KVXER7	Exécuté	Achat	40	HR.UN-C	10,15	10,15	10,30	406,00	1 juin 2023	
         230601030558	5KVXEY9	Exécuté	Vente	40	LSPD-C	19,24	19,24	19,81	769,60	1 juin 2023	
         230601030493	5KVXER7	Exécuté	Vente	10	SHOP-C	78,49	78,49	78,10	784,90	1 juin 2023	
         230601030371	5KVXEY9	Exécuté	Vente	5	SHOP-C	78,40	78,42	78,10	392,10	1 juin 2023	
         230601030340	5KVXER7	Exécuté	Vente	10	SHOP-C	78,39	78,39	78,10	783,90	1 juin 2023	
         230601029959	5KVXER7	Exécuté	Vente	5	NVEI-C	42,16	42,16	41,80	210,80	1 juin 2023	
         230601029286	5KVXER7	Exécuté	Vente	40	LSPD-C	19,17	19,17	19,81	766,80	1 juin 2023	
         230601025053	5KVXEY9	Exécuté	Vente	5	NVEI-C	42,14	42,14	41,80	210,70	1 juin 2023	
         230601025012	5KVXER7	Exécuté	Vente	10	NVEI-C	42,13	42,13	41,80	421,30	1 juin 2023	
         230601024774	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,24	10,24	10,30	409,60	1 juin 2023	
         230601024683	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,25	10,25	10,30	410,00	1 juin 2023	
         230601019272	5KVXER7	Exécuté	Achat	200	LEV-C	2,51	2,51	2,52	502,00	1 juin 2023	
         230601019142	5KVXEY9	Exécuté	Achat	100	LEV-C	2,51	2,51	2,52	251,00	1 juin 2023	
         230601018998	5KVXER7	Exécuté	Vente	40	LSPD-C	18,99	18,99	19,81	759,60	1 juin 2023	
         230601018757	5KVXEY9	Exécuté	Vente	40	LSPD-C	18,94	18,94	19,81	757,60	1 juin 2023	
         230601018679	5KVXER7	Exécuté	Vente	20	HR.UN-C	10,22	10,22	10,30	204,40	1 juin 2023	
         230601018649	5KVXER7	Exécuté	Vente	40	LSPD-C	18,92	18,93	19,81	757,20	1 juin 2023	
         230601017259	5KVXEY9	Exécuté	Vente	30	LSPD-C	18,86	18,86	19,81	565,80	1 juin 2023	
         230601017158	5KVXEZ7	Exécuté	Vente	20	LSPD-C	18,84	18,84	19,81	376,80	1 juin 2023	
         230601016009	5KVXEY9	Exécuté	Vente	20	HR.UN-C	10,20	10,20	10,30	204,00	1 juin 2023	
         230601015399	5KVXER7	Exécuté	Vente	20	HR.UN-C	10,20	10,20	10,30	204,00	1 juin 2023	
         230601011969	5KVXEY9	Exécuté	Achat	2	NVEI-C	41,30	41,30	41,80	82,60	1 juin 2023	
         230601011749	5KVXER7	Exécuté	Vente	5	SHOP-C	77,17	77,20	78,10	386,00	1 juin 2023	
         230601011645	5KVXEY9	Exécuté	Vente	3	SHOP-C	77,20	77,20	78,10	231,60	1 juin 2023	
         230601011401	5KVXEY9	Exécuté	Achat	10	NVEI-C	41,30	41,30	41,80	413,00	1 juin 2023	
         230601011284	5KVXER7	Exécuté	Achat	10	NVEI-C	41,28	41,28	41,80	412,80	1 juin 2023	
         230601011007	5KVXEY9	Exécuté	Achat	10	NVEI-C	41,32	41,32	41,80	413,20	1 juin 2023	
         230601010920	5KVXEY9	Exécuté	Achat	10	NVEI-C	41,32	41,32	41,80	413,20	1 juin 2023	
         230601010741	5KVXEY9	Exécuté	Vente	30	LSPD-C	18,56	18,57	19,81	557,10	1 juin 2023	
         230601010665	5KVXER7	Exécuté	Vente	30	LSPD-C	18,57	18,57	19,81	557,10	1 juin 2023	
         230601010594	5KVXER7	Exécuté	Achat	5	NVEI-C	41,37	41,37	41,80	206,85	1 juin 2023	
         230601010387	5KVXER7	Exécuté	Achat	12	NVEI-C	41,43	41,43	41,80	497,16	1 juin 2023	
         230601010313	5KVXER7	Exécuté	Vente	30	LSPD-C	18,55	18,55	19,81	556,50	1 juin 2023	
         230601008393	5KVXEY9	Exécuté	Vente	40	LSPD-C	18,43	18,43	19,81	737,20	1 juin 2023	
         230601008295	5KVXER7	Exécuté	Achat	10	NVEI-C	41,44	41,44	41,80	414,40	1 juin 2023	
         230601008221	5KVXER7	Exécuté	Achat	10	NVEI-C	41,45	41,45	41,80	414,50	1 juin 2023	
         230601008108	5KVXER7	Exécuté	Vente	40	LSPD-C	18,40	18,40	19,81	736,00	1 juin 2023	
         230601007388	5KVXEY9	Exécuté	Achat	4	NVEI-C	41,73	41,67	41,80	166,68	1 juin 2023	
         230601007273	5KVXER7	Exécuté	Achat	5	NVEI-C	41,73	41,71	41,80	208,55	1 juin 2023	
         230601006952	5KVXER7	Exécuté	Achat	5	NVEI-C	41,81	41,81	41,80	209,05	1 juin 2023	
         230601006390	5KVXEY9	Exécuté	Achat	3	SHOP-C	75,63	75,63	78,10	226,89	1 juin 2023	
         230601006336	5KVXER7	Exécuté	Achat	5	SHOP-C	75,66	75,66	78,10	378,30	1 juin 2023 

 
         230614054018	5KVXER7	Exécuté	Vente	110	CTS-C	3,22	3,22	354,20	14 juin 2023	
         230614048899	5KVXER7	Exécuté	Achat	110	CTS-C	3,15	3,15	346,50	14 juin 2023	
         230614047646	5KVXER7	Exécuté	Vente	10	NWC-C	33,97	33,97	339,70	14 juin 2023	
         230614047500	5KVXER7	Exécuté	Achat	90	CTS-C	3,15	3,15	283,50	14 juin 2023	
         230614046304	5KVXER7	Exécuté	Achat	10	NWC-C	33,92	33,92	339,20	14 juin 2023	
         230614042194	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,21	3,21	642,00	14 juin 2023	
         230614042076	5KVXER7	Exécuté	Achat	85	LEV-C	2,81	2,81	238,85	14 juin 2023	
         230614041943	5KVXEY9	Exécuté	Achat	10	NVEI-C	39,56	39,56	395,60	14 juin 2023	
         230614041680	5KVXEY9	Exécuté	Achat	5	NVEI-C	39,57	39,57	197,85	14 juin 2023	
         230614041548	5KVXER7	Exécuté	Achat	10	NVEI-C	39,55	39,55	395,50	14 juin 2023	
         230614041466	5KVXEY9	Exécuté	Vente	200	INO.UN-C	3,21	3,21	642,00	14 juin 2023	
         230614041424	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,21	3,21	642,00	14 juin 2023	
         230614041250	5KVXEZ7	Exécuté	Achat	2	NVEI-C	39,67	39,67	79,34	14 juin 2023	
         230614041196	5KVXEZ7	Exécuté	Vente	25	INO.UN-C	3,21	3,21	80,25	14 juin 2023	
         230614038607	5KVXEY9	Exécuté	Achat	90	CTS-C	3,17	3,17	285,30	14 juin 2023	
         230614038300	5KVXEY9	Exécuté	Vente	100	LEV-C	2,83	2,83	283,00	14 juin 2023	
         230614038251	5KVXEY9	Exécuté	Achat	90	CTS-C	3,19	3,18	286,20	14 juin 2023	
         230614038175	5KVXEY9	Exécuté	Vente	100	LEV-C	2,83	2,83	283,00	14 juin 2023	
         230614037688	5KVXER7	Exécuté	Achat	5	CTS-C	3,20	3,20	16,00	14 juin 2023	
         230614036868	5KVXEY9	Exécuté	Achat	70	CTS-C	3,21	3,21	224,70	14 juin 2023	
         230614036549	5KVXEZ7	Exécuté	Achat	100	CTS-C	3,20	3,20	320,00	14 juin 2023	
         230614036399	5KVXER7	Exécuté	Achat	70	CTS-C	3,20	3,20	224,00	14 juin 2023	
         230614036360	5KVXEZ7	Exécuté	Vente	8	NVEI-C	40,05	40,05	320,40	14 juin 2023	
         230614036006	5KVXEY9	Exécuté	Achat	100	CTS-C	3,21	3,21	321,00	14 juin 2023	
         230614035961	5KVXER7	Exécuté	Achat	100	CTS-C	3,21	3,21	321,00	14 juin 2023	
         230614035804	5KVXER7	Exécuté	Achat	200	LEV-C	2,83	2,83	566,00	14 juin 2023	
         230614035770	5KVXEY9	Exécuté	Achat	200	LEV-C	2,83	2,83	566,00	14 juin 2023	
         230614035610	5KVXEY9	Exécuté	Vente	32	NWC-C	34,11	34,11	1 091,52	14 juin 2023	
         230614035567	5KVXER7	Exécuté	Vente	33	NWC-C	34,10	34,10	1 125,30	14 juin 2023	
         230614035158	5KVXEZ7	Exécuté	Achat	8	NVEI-C	39,92	39,92	319,36	14 juin 2023	
         230614035103	5KVXEZ7	Exécuté	Vente	100	INO.UN-C	3,21	3,21	321,00	14 juin 2023	
         230614032457	5KVXEZ7	Exécuté	Achat	30	LEV-C	2,85	2,84	85,20	14 juin 2023	
         230614030040	5KVXEZ7	Exécuté	Achat	100	CTS-C	3,21	3,205	320,50	14 juin 2023	
         230614029828	5KVXER7	Exécuté	Achat	50	INO.UN-C	3,22	3,22	161,00	14 juin 2023	
         230614029759	5KVXEZ7	Exécuté	Vente	10	NVEI-C	40,34	40,34	403,40	14 juin 2023	
         230614029595	5KVXEZ7	Exécuté	Achat	125	INO.UN-C	3,22	3,22	402,50	14 juin 2023	
         230614029498	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,21	3,21	642,00	14 juin 2023	
         230614029455	5KVXER7	Exécuté	Vente	20	NVEI-C	40,25	40,25	805,00	14 juin 2023	
         230614027918	5KVXEZ7	Exécuté	Vente	10	NVEI-C	40,26	40,26	402,60	14 juin 2023	
         230614027800	5KVXEZ7	Exécuté	Achat	125	CTS-C	3,21	3,21	401,25	14 juin 2023	
         230614027400	5KVXER7	Exécuté	Achat	70	CTS-C	3,20	3,20	224,00	14 juin 2023	
         230614027355	5KVXER7	Exécuté	Achat	100	CTS-C	3,21	3,205	320,50	14 juin 2023	
         230614027334	5KVXER7	Exécuté	Achat	100	LEV-C	2,85	2,85	285,00	14 juin 2023	
         230614027276	5KVXER7	Exécuté	Vente	20	NVEI-C	40,26	40,26	805,20	14 juin 2023	
         230614027076	5KVXEZ7	Exécuté	Vente	10	NVEI-C	40,26	40,26	402,60	14 juin 2023	
         230614025395	5KVXER7	Exécuté	Achat	100	INO.UN-C	3,24	3,24	324,00	14 juin 2023	
         230614023941	5KVXER7	Exécuté	Achat	100	LEV-C	2,86	2,86	286,00	14 juin 2023	
         230614021033	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,24	3,24	648,00	14 juin 2023	
         230614020250	5KVXEY9	Exécuté	Achat	150	INO.UN-C	3,24	3,24	486,00	14 juin 2023	
         230614020181	5KVXER7	Exécuté	Achat	150	INO.UN-C	3,24	3,24	486,00	14 juin 2023	
         230614019716	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,24	3,24	648,00	14 juin 2023	
         230614019630	5KVXER7	Exécuté	Vente	30	NVEI-C	40,28	40,28	1 208,40	14 juin 2023	
         230614019475	5KVXEY9	Exécuté	Achat	100	INO.UN-C	3,25	3,25	325,00	14 juin 2023	
         230614019062	5KVXEY9	Exécuté	Vente	20	NVEI-C	40,30	40,30	806,00	14 juin 2023	
         230614019017	5KVXER7	Exécuté	Vente	30	NVEI-C	40,32	40,32	1 209,60	14 juin 2023	
         230614017929	5KVXEY9	Exécuté	Achat	2	NWC-C	33,91	33,91	67,82	14 juin 2023	
         230614017874	5KVXEY9	Exécuté	Vente	4	LSPD-C	21,74	21,75	87,00	14 juin 2023	
         230614015820	5KVXER7	Exécuté	Achat	13	NWC-C	33,94	33,93	441,09	14 juin 2023	
         230614015741	5KVXER7	Exécuté	Vente	10	NVEI-C	40,06	40,06	400,60	14 juin 2023	
         230614012709	5KVXEY9	Exécuté	Achat	4	LSPD-C	21,45	21,45	85,80	14 juin 2023	
         230614011688	5KVXEY9	Exécuté	Achat	10	NWC-C	34,07	34,07	340,70	14 juin 2023	
         230614010558	5KVXER7	Exécuté	Achat	10	NVEI-C	40,01	40,01	400,10	14 juin 2023	
         230614008422	5KVXEY9	Exécuté	Achat	20	NWC-C	34,11	34,11	682,20	14 juin 2023	
         230614008361	5KVXER7	Exécuté	Achat	20	NWC-C	34,11	34,11	682,20	14 juin 2023	
         230614006231	5KVXER7	Exécuté	Vente	25	NVEI-C	40,35	40,35	1 008,75	14 juin 2023	
         230614006024	5KVXEY9	Exécuté	Vente	5	SHOP-C	86,42	86,42	432,10	14 juin 2023	
         230614005826	5KVXEY9	Exécuté	Vente	5	SHOP-C	86,54	86,54	432,70	14 juin 2023
         
         230615055277	5KVXER7	Exécuté	Achat	20	NVEI-C	39,91	39,91	798,20	15 juin 2023	
         230615054265	5KVXER7	Exécuté	Vente	30	NVEI-C	39,98	39,99	1 199,70	15 juin 2023	
         230615053576	5KVXER7	Exécuté	Achat	12	NWC-C	32,96	32,95	395,40	15 juin 2023	
         230615053543	5KVXER7	Exécuté	Vente	10	NVEI-C	39,90	39,90	399,00	15 juin 2023	
         230615053455	5KVXEY9	Exécuté	Achat	12	NWC-C	33,00	33,00	396,00	15 juin 2023	
         230615052537	5KVXEY9	Exécuté	Achat	6	NWC-C	33,08	33,08	198,48	15 juin 2023	
         230615051052	5KVXEY9	Exécuté	Vente	5	NVEI-C	39,81	39,81	199,05	15 juin 2023	
         230615050941	5KVXEY9	Exécuté	Vente	10	NVEI-C	39,85	39,85	398,50	15 juin 2023	
         230615050828	5KVXER7	Exécuté	Achat	5	LEV-C	2,68	2,68	13,40	15 juin 2023	
         230615047641	5KVXEZ7	Exécuté	Achat	4	LEV-C	2,71	2,71	10,84	15 juin 2023	
         230615045634	5KVXER7	Exécuté	Achat	10	NWC-C	33,11	33,11	331,10	15 juin 2023	
         230615045597	5KVXER7	Exécuté	Achat	10	NWC-C	33,11	33,11	331,10	15 juin 2023	
         230615045550	5KVXER7	Exécuté	Vente	17	NVEI-C	39,65	39,67	674,39	15 juin 2023	
         230615038863	5KVXER7	Exécuté	Achat	5	LEV-C	2,71	2,70	13,50	15 juin 2023	
         230615037349	5KVXER7	Exécuté	Achat	140	LEV-C	2,71	2,71	379,40	15 juin 2023	
         230615037282	5KVXER7	Exécuté	Achat	100	LEV-C	2,71	2,71	271,00	15 juin 2023	
         230615037071	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,27	3,27	654,00	15 juin 2023	
         230615036159	5KVXEZ7	Exécuté	Achat	2	NWC-C	33,38	33,38	66,76	15 juin 2023	
         230615035633	5KVXEZ7	Exécuté	Vente	2	NVEI-C	39,48	39,48	78,96	15 juin 2023	
         230615035544	5KVXER7	Exécuté	Achat	20	NWC-C	33,40	33,40	668,00	15 juin 2023	
         230615035501	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,26	3,26	652,00	15 juin 2023	
         230615028491	5KVXER7	Exécuté	Achat	9	NWC-C	33,63	33,63	302,67	15 juin 2023	
         230615028423	5KVXER7	Exécuté	Vente	100	INO.UN-C	3,22	3,22	322,00	15 juin 2023	
         230615024199	5KVXER7	Exécuté	Achat	55	LEV-C	2,74	2,74	150,70	15 juin 2023	
         230615024117	5KVXEY9	Exécuté	Achat	14	LEV-C	2,74	2,74	38,36	15 juin 2023	
         230615023860	5KVXEY9	Exécuté	Vente	1	NVEI-C	39,13	39,13	39,13	15 juin 2023	
         230615023823	5KVXER7	Exécuté	Vente	4	NVEI-C	39,14	39,14	156,56	15 juin 2023	
         230615021486	5KVXEZ7	Exécuté	Achat	1	LEV-C	2,75	2,75	2,75	15 juin 2023	
         230615021436	5KVXEZ7	Exécuté	Achat	1	CTS-C	3,17	3,17	3,17	15 juin 2023	
         230615021368	5KVXEY9	Exécuté	Achat	3	CTS-C	3,17	3,17	9,51	15 juin 2023	
         230615021322	5KVXER7	Exécuté	Achat	6	CTS-C	3,17	3,17	19,02	15 juin 2023	
         230615017800	5KVXEY9	Exécuté	Achat	1	NVEI-C	38,68	38,68	38,68	15 juin 2023	
         230615017733	5KVXER7	Exécuté	Achat	1	NVEI-C	38,69	38,69	38,69	15 juin 2023	
         230615016373	5KVXER7	Exécuté	Achat	100	INO.UN-C	3,17	3,17	317,00	15 juin 2023		
         230616035820	5KVXER7	Exécuté	Achat	1	NVEI-C	38,41	38,41	38,00	38,41	16 juin 2023	
         230616035718	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,37	3,37	3,34	674,00	16 juin 2023	
         230616035524	5KVXER7	Exécuté	Achat	20	NWC-C	32,88	32,87	32,26	657,40	16 juin 2023	
         230616035480	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,37	3,37	3,34	674,00	16 juin 2023	
         230616032997	5KVXER7	Exécuté	Achat	10	NWC-C	33,08	33,08	32,26	330,80	16 juin 2023	
         230616032949	5KVXER7	Exécuté	Vente	9	NVEI-C	38,44	38,44	38,00	345,96	16 juin 2023	
         230616032857	5KVXER7	Exécuté	Achat	10	NWC-C	33,08	33,08	32,26	330,80	16 juin 2023	
         230616030278	5KVXER7	Exécuté	Achat	1	NVEI-C	38,39	38,38	38,00	38,38	16 juin 2023	
         230616030172	5KVXER7	Exécuté	Vente	1	CTS-C	3,22	3,22	3,19	3,22	16 juin 2023	
         230616026163	5KVXEY9	Exécuté	Achat	7	LEV-C	2,66	2,66	2,62	18,62	16 juin 2023	
         230616025190	5KVXER7	Exécuté	Achat	8	NVEI-C	38,21	38,20	38,00	305,60	16 juin 2023	
         230616025140	5KVXER7	Exécuté	Vente	100	INO.UN-C	3,36	3,36	3,34	336,00	16 juin 2023	
         230616023819	5KVXER7	Exécuté	Vente	10	NWC-C	33,31	33,31	32,26	333,10	16 juin 2023	
         230616023747	5KVXER7	Exécuté	Achat	3	NVEI-C	38,64	38,63	38,00	115,89	16 juin 2023	
         230616023552	5KVXEY9	Exécuté	Achat	7	NVEI-C	38,69	38,69	38,00	270,83	16 juin 2023	
         230616023447	5KVXEY9	Exécuté	Achat	10	NVEI-C	38,68	38,68	38,00	386,80	16 juin 2023	
         230616023289	5KVXER7	Exécuté	Achat	15	NVEI-C	38,69	38,69	38,00	580,35	16 juin 2023	
         230616023067	5KVXER7	Exécuté	Achat	10	NWC-C	38,71	33,22	32,26	332,20	16 juin 2023	
         230616020082	5KVXER7	Exécuté	Vente	31	NWC-C	33,48	33,48	32,26	1 037,88	16 juin 2023	
         230616019819	5KVXEY9	Exécuté	Vente	8	NWC-C	33,48	33,48	32,26	267,84	16 juin 2023	
         230616019492	5KVXEY9	Exécuté	Achat	99	LEV-C	2,67	2,67	2,62	264,33	16 juin 2023	
         230616018399	5KVXEY9	Exécuté	Vente	125	CTS-C	3,27	3,27	3,19	408,75	16 juin 2023	
         230616018129	5KVXEY9	Exécuté	Achat	10	NVEI-C	38,68	38,68	38,00	386,80	16 juin 2023	
         230616017244	5KVXEY9	Exécuté	Vente	200	CTS-C	3,26	3,26	3,19	652,00	16 juin 2023	
         230616013885	5KVXEY9	Exécuté	Achat	15	LEV-C	2,68	2,68	2,62	40,20	16 juin 2023	
         230616013665	5KVXEY9	Exécuté	Vente	1	NVEI-C	39,01	39,01	38,00	39,01	16 juin 2023	
         230616012883	5KVXER7	Exécuté	Achat	10	LEV-C	2,68	2,68	2,62	26,80	16 juin 2023	
         230616012829	5KVXEY9	Exécuté	Achat	10	LEV-C	2,68	2,68	2,62	26,80	16 juin 2023	
         230616012764	5KVXEY9	Exécuté	Achat	1	NVEI-C	38,89	38,88	38,00	38,88	16 juin 2023	
         230616012026	5KVXEY9	Exécuté	Achat	20	NVEI-C	39,13	39,12	38,00	782,40	16 juin 2023	
         230616011218	5KVXEY9	Exécuté	Achat	20	NVEI-C	39,21	39,21	38,00	784,20	16 juin 2023	
         230616010059	5KVXEY9	Exécuté	Achat	10	NVEI-C	39,23	39,23	38,00	392,30	16 juin 2023	
         230616007469	5KVXER7	Exécuté	Achat	2	NVEI-C	39,47	39,47	38,00	78,94	16 juin 2023	
         230616007247	5KVXER7	Exécuté	Achat	10	NVEI-C	39,52	39,52	38,00	395,20	16 juin 2023 
         230619015234	5KVXEY9	Exécuté	Vente	300	INO.UN-C	3,43	3,43	3,39	1 029,00	19 juin 2023	
         230619015224	5KVXER7	Exécuté	Vente	400	INO.UN-C	3,43	3,43	3,39	1 372,00	19 juin 2023	
         230619014615	5KVXEY9	Exécuté	Vente	100	INO.UN-C	3,39	3,39	3,39	339,00	19 juin 2023	
         230619014596	5KVXER7	Exécuté	Vente	400	INO.UN-C	3,39	3,39	3,39	1 356,00	19 juin 2023	
         230619014568	5KVXER7	Exécuté	Vente	400	INO.UN-C	3,39	3,39	3,39	1 356,00	19 juin 2023	
         230619014558	5KVXER7	Exécuté	Vente	100	INO.UN-C	3,40	3,40	3,39	340,00	19 juin 2023	
         230619011517	5KVXEY9	Exécuté	Achat	5	NWC-C	32,68	32,68	32,55	163,40	19 juin 2023	
         230619007360	5KVXEY9	Exécuté	Vente	5	NWC-C	3,86	32,84	32,55	164,20	19 juin 2023	
         230619006768	5KVXEZ7	Exécuté	Vente	41	CTS-C	3,29	3,29	3,25	134,89	19 juin 2023
         
         230620047596	5KVXEZ7	Exécuté	Achat	41	INO.UN-C	3,31	3,31	3,30	135,71	20 juin 2023	
         230620047493	5KVXEZ7	Exécuté	Vente	42	CTS-C	3,26	3,26	3,265	136,92	20 juin 2023	
         230620047387	5KVXER7	Exécuté	Achat	196	INO.UN-C	3,31	3,31	3,30	648,76	20 juin 2023	
         230620047267	5KVXER7	Exécuté	Vente	190	CTS-C	3,26	3,26	3,265	619,40	20 juin 2023	
         230620046875	5KVXEY9	Exécuté	Achat	110	INO.UN-C	3,31	3,31	3,30	364,10	20 juin 2023	
         230620045409	5KVXEY9	Exécuté	Vente	10	NVEI-C	36,78	36,78	36,78	367,80	20 juin 2023	
         230620045324	5KVXER7	Exécuté	Achat	40	INO.UN-C	3,31	3,31	3,30	132,40	20 juin 2023	
         230620045221	5KVXER7	Exécuté	Achat	59	INO.UN-C	3,31	3,31	3,30	195,29	20 juin 2023	
         230620045128	5KVXER7	Exécuté	Achat	5	NWC-C	32,41	32,41	32,43	162,05	20 juin 2023	
         230620044206	5KVXER7	Exécuté	Achat	200	INO.UN-C	3,32	3,32	3,30	664,00	20 juin 2023	
         230620044164	5KVXER7	Exécuté	Vente	25	NWC-C	32,53	32,53	32,43	813,25	20 juin 2023	
         230620044077	5KVXEY9	Exécuté	Achat	195	INO.UN-C	3,31	3,31	3,30	645,45	20 juin 2023	
         230620043998	5KVXEY9	Exécuté	Vente	20	NWC-C	32,53	32,53	32,43	650,60	20 juin 2023	
         230620040783	5KVXER7	Exécuté	Vente	10	NVEI-C	36,73	36,73	36,78	367,30	20 juin 2023	
         230620040682	5KVXEY9	Exécuté	Achat	6	LSPD-C	21,02	21,01	21,065	126,06	20 juin 2023	
         230620040652	5KVXEY9	Exécuté	Achat	10	LSPD-C	21,00	21,00	21,065	210,00	20 juin 2023	
         230620040574	5KVXEY9	Exécuté	Vente	30	HR.UN-C	10,31	10,31	10,27	309,30	20 juin 2023	
         230620039488	5KVXER7	Exécuté	Achat	1	LSPD-C	21,04	21,03	21,065	21,03	20 juin 2023	
         230620039472	5KVXER7	Exécuté	Achat	1	LSPD-C	21,04	21,04	21,065	21,04	20 juin 2023	
         230620038615	5KVXER7	Exécuté	Achat	12	NWC-C	32,40	32,40	32,43	388,80	20 juin 2023	
         230620038585	5KVXEY9	Exécuté	Achat	9	NWC-C	32,40	32,40	32,43	291,60	20 juin 2023	
         230620038553	5KVXEY9	Exécuté	Achat	20	NWC-C	32,40	32,40	32,43	648,00	20 juin 2023	
         230620038431	5KVXEY9	Exécuté	Vente	20	NVEI-C	36,82	36,82	36,78	736,40	20 juin 2023	
         230620038295	5KVXEY9	Exécuté	Vente	6	NVEI-C	36,72	36,72	36,78	220,32	20 juin 2023	
         230620034073	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,30	10,30	10,27	412,00	20 juin 2023	
         230620033868	5KVXER7	Exécuté	Achat	1	NWC-C	32,41	32,40	32,43	32,40	20 juin 2023	
         230620033836	5KVXEY9	Exécuté	Achat	7	NWC-C	33,41	32,40	32,43	226,80	20 juin 2023	
         230620033438	5KVXEY9	Exécuté	Vente	12	LEV-C	2,57	2,57	2,57	30,84	20 juin 2023	
         230620033419	5KVXER7	Exécuté	Vente	13	LEV-C	2,57	2,57	2,57	33,41	20 juin 2023	
         230620033148	5KVXER7	Exécuté	Achat	12	NWC-C	32,42	32,42	32,43	389,04	20 juin 2023	
         230620032280	5KVXEY9	Exécuté	Vente	20	HR.UN-C	10,30	10,30	10,27	206,00	20 juin 2023	
         230620032185	5KVXER7	Exécuté	Vente	40	HR.UN-C	10,30	10,30	10,27	412,00	20 juin 2023	
         230620017673	5KVXER7	Exécuté	Achat	13	LEV-C	2,54	2,54	2,57	33,02	20 juin 2023	
         230620017594	5KVXEY9	Exécuté	Achat	12	LEV-C	2,55	2,54	2,57	30,48	20 juin 2023	
         230620017175	5KVXEY9	Exécuté	Achat	10	NVEI-C	36,37	36,37	36,78	363,70	20 juin 2023	
         230620016808	5KVXEY9	Exécuté	Achat	8	NVEI-C	36,45	36,44	36,78	291,52	20 juin 2023	
         230620016741	5KVXEY9	Exécuté	Vente	200	INO.UN-C	3,38	3,38	3,30	676,00	20 juin 2023	
         230620016640	5KVXER7	Exécuté	Achat	18	NVEI-C	36,47	36,47	36,78	656,46	20 juin 2023	
         230620016546	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,38	3,38	3,30	676,00	20 juin 2023	
         230620016060	5KVXEY9	Exécuté	Achat	1	NVEI-C	36,47	36,47	36,78	36,47	20 juin 2023	
         230620015566	5KVXEY9	Exécuté	Achat	18	NVEI-C	36,46	36,46	36,78	656,28	20 juin 2023	
         230620015525	5KVXEY9	Exécuté	Vente	200	INO.UN-C	3,37	3,37	3,30	674,00	20 juin 2023	
         230620015430	5KVXER7	Exécuté	Achat	9	NVEI-C	36,46	36,46	36,78	328,14	20 juin 2023	
         230620015291	5KVXER7	Exécuté	Achat	10	NVEI-C	36,64	36,64	36,78	366,40	20 juin 2023	
         230620015234	5KVXER7	Exécuté	Vente	200	INO.UN-C	3,37	3,37	3,30	674,00	20 juin 2023	
         230620012424	5KVXER7	Exécuté	Achat	8	NVEI-C	37,06	37,06	36,78	296,48	20 juin 2023	
         230620011729	5KVXER7	Exécuté	Achat	10	NVEI-C	37,10	37,10	36,78	371,00	20 juin 2023	
         230620009761	5KVXER7	Exécuté	Achat	10	NVEI-C	37,29	37,29	36,78	372,90	20 juin 2023	
         230620009503	5KVXER7	Exécuté	Achat	10	NVEI-C	37,30	37,30	36,78	373,00	20 juin 2023	
         230620009239	5KVXER7	Exécuté	Achat	10	NVEI-C	37,39	37,39	36,78	373,90	20 juin 2023	
         230620007549	5KVXEY9	Exécuté	Achat	9	NVEI-C	37,52	37,52	36,78	337,68	20 juin 2023	
         230620007418	5KVXER7	Exécuté	Achat	20	NVEI-C	37,51	37,51	36,78	750,20	20 juin 2023	
         230620006877	5KVXEY9	Exécuté	Achat	200	INO.UN-C	3,36	3,36	3,30	672,00	20 juin 2023	
         230620006529	5KVXER7	Exécuté	Achat	300	INO.UN-C	3,36	3,36	3,30	1 008,00	20 juin 2023	
         230620006432	5KVXER7	Exécuté	Achat	300	INO.UN-C	3,37	3,37	3,30	1 011,00	20 juin 2023	
         230620006371	5KVXEY9	Exécuté	Achat	200	INO.UN-C	3,37	3,37	3,30	674,00	20 juin 2023	
         230620006092	5KVXER7	Exécuté	Achat	10	NVEI-C	37,80	37,80	36,78	378,00	20 juin 2023	
         230620005025	5KVXEZ7	Exécuté	Achat	42	CTS-C	3,21	3,21	3,265	134,82	20 juin 2023	

             """;
 
 }
