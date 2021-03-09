from hdfs import InsecureClient
from avro import schema
from avro.datafile import DataFileReader, DataFileWriter
from avro.io import DatumReader, DatumWriter

client = InsecureClient('http://192.168.1.155:50070', user='cloudera')

# чтение airlines.dat из hdfs
with client.read(hdfs_path='/user/student/airlines/airlines.dat') as reader:
    airlines = reader.read()

text = str(airlines, 'UTF-8')

# print(text)

# сохранение airlines.csv
with open('./airlines.csv', mode='w', encoding='UTF-8') as f:
    f.write(text)

# конвертирование airlines.csv в airlines.avro
schema = schema.parse(open("airlines.avsc", "rb").read())

writer = DataFileWriter(open("airlines.avro", "wb"), DatumWriter(), schema)
for line in text.splitlines():
    print(line)
    fields = line.split(',')
    writer.append(
        {
            "field0": int(fields[0]),
            "field1": fields[1],
            "field2": fields[2],
            "field3": fields[3],
            "field4": fields[4],
            "field5": fields[5],
            "field6": fields[6],
            "field7": fields[7]
        }
    )
writer.close()

# reader = DataFileReader(open("airlines.avro", "rb"), DatumReader())
# for user in reader:
#     print(user)
# reader.close()

# запись airlines.avro в hdfs
with client.write(hdfs_path='/user/student/airlines/airlines.avro') as writer:
    with open('./airlines.avro', mode='rb') as f:
        writer.write(f.read())
