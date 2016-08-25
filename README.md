Web scraper that useful for parse some information from array of pages.
Initially there is implemented HhVacancy class that parse vacancies information
from hh.ru/vacancy/<idx>.

Clean app:

> mvn clean

Build app:

> mvn package

Usage:

> mvn exec:java -Dexec.args="ParserClassName BaseURL firstIdx lastIdx"

Example of usage to scrap pages from hh.ru/vacancy/8000000 to hh.ru/vacancy/9000000:

> mvn exec:java -Dexec.args="HhVacancy https://hh.ru/vacancy/ 8000000 9000000"

Resulted files are TAB delimited TXT files that contain vacancies info. One row - one vacancy page.
Columns of file:
	- Vacancy URL
	- Publication date
	- Salary
	- City
	- Years of experience
	- Vacancy title
	- Organization name
	- Organization link
	- Vacancy mode
	- Required skills
	- Vacancy description

Perform some analysis of resulted files:

> awk -f advice.txt hh_ru_vacancy_*

Future work:
adding support to scrap less structured web sites like LinkedIn where
Web Crawl work needs to prepare array of links.

