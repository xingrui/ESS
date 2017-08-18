import sqlite3

def dump_table(table_name):
    conn = sqlite3.connect('../test.db')
    c = conn.cursor()
    cursor = c.execute('''select * from %s''' % table_name)
    for row in cursor:
        print row
    conn.close()

def main():
    table_name_list = ['admin', 'request', 'control', 'status']
    for table_name in table_name_list:
        dump_table(table_name)

if __name__ == "__main__":
    main()
