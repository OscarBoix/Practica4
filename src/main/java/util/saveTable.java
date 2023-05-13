package util;/*
  Copiar y usar donde corresponda
*/
/*    public void saveTable(Table t, String filename) throws IOException {
        try {
            FileWriter fw = new FileWriter(filename);
            for (int i=0; i<t.getNumRows(); i++)
            {
                Row row = t.getRowAt(i);
                List<Double> datos = row.getData();
                int j=0;
                for (; j<datos.size()-1; j++)
                {
                    fw.write(datos.get(j).toString());
                    fw.write(",");
                }
                fw.write(datos.get(j).toString());
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            throw e;
        }
    }

/*
  Posible contexto de uso
*/
/*
    void savePredictions()
    {
        Table datos_out = new Table();
        for (int i=0; i<datos.getNumRows(); i++) {
            List<Double> data = datos.getRowAt(i).getData();
            data.add((double)kmeans.estimate(data));
            datos_out.addRow(data);
        }
        try {
            saveTable(datos_out, dataset + "_out.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/