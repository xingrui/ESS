import sqlite3
conn = sqlite3.connect('../test.db')
c = conn.cursor()
c.execute('''CREATE TABLE IF NOT EXISTS admin(
            adminname VARCHAR(20) NOT NULL PRIMARY KEY,
            password VARCHAR(20) NOT NULL
            );''')
c.execute('''REPLACE INTO admin VALUES('root','123456');''')
c.execute('''CREATE TABLE IF NOT EXISTS request(
            floor INT NOT NULL,
            type VARCHAR(4) NOT NULL,
            time DATETIME NOT NULL
            );''')
c.execute('''CREATE TABLE IF NOT EXISTS control(
            elevatorID INT NOT NULL,
            time DATETIME NOT NULL,
            event VARCHAR(15) NOT NULL
            );''')
c.execute('''CREATE TABLE IF NOT EXISTS status(
            elevatorID INT PRIMARY KEY NOT NULL,
            status VARCHAR(10) NOT NULL,
            currentfloor INT NOT NULL
            );''')
conn.commit()
conn.close()
