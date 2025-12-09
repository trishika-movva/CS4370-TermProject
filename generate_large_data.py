import csv
import datetime
import random

CSV_PATH = "data_job_posts.csv"
OUT_SQL = "data_large.sql"
MAX_JOBS = 1500
COMPANY_ID_START = 1001
JOB_ID_START = 10001

def get_season(month):
    if 3 <= month <= 5:
        return "Spring"
    elif 6 <= month <= 8:
        return "Summer"
    elif 9 <= month <= 11:
        return "Fall"
    else:
        return "Winter"

def esc(s):
    return s.replace("'", "''")

def main():
    company_map = {}
    company_rows = []
    job_rows = []

    next_company_id = COMPANY_ID_START
    next_job_id = JOB_ID_START

    with open(CSV_PATH, newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            if len(job_rows) >= MAX_JOBS:
                break

            title = (row.get("Title") or "").strip()
            company_name = (row.get("Company") or "").strip()
            location = (row.get("Location") or "").strip()
            year_str = str(row.get("Year") or "").strip()
            month_str = str(row.get("Month") or "").strip()

            if not title or not company_name:
                continue
            if not year_str.isdigit() or not month_str.isdigit():
                continue

            year = int(year_str)
            month = int(month_str)

            try:
                posting_date = datetime.date(year, month, 1)
            except:
                continue

            deadline = posting_date + datetime.timedelta(days=60)
            season = get_season(month)
            employment_type = "Full-time"
            job_level = random.choice(["Intern", "Entry", "Mid", "Senior"])

            if not location:
                location = "Unknown"
            
            # Truncate location to 255 characters to match database column size
            location = location[:255] if len(location) > 255 else location

            if company_name not in company_map:
                company_id = next_company_id
                company_map[company_name] = company_id
                next_company_id += 1
                industry = "Unknown"
                # Truncate hq_location to 255 characters to match database column size
                hq_location = location[:255] if len(location) > 255 else location
                website = None
                company_rows.append((company_id, company_name, industry, hq_location, website))
            else:
                company_id = company_map[company_name]

            job_id = next_job_id
            next_job_id += 1

            job_rows.append(
                (
                    job_id,
                    company_id,
                    esc(title),
                    esc(location),
                    employment_type,
                    job_level,
                    posting_date.isoformat(),
                    deadline.isoformat(),
                    season,
                    1
                )
            )

    with open(OUT_SQL, "w", encoding="utf-8") as out:
        out.write("USE application_tracker;\n\n")

        for cid, name, industry, hq, website in company_rows:
            name2 = esc(name)
            industry2 = esc(industry)
            hq2 = esc(hq)
            website_sql = "NULL"
            out.write(
                f"INSERT INTO company (company_id, name, industry, hq_location, website) "
                f"VALUES ({cid}, '{name2}', '{industry2}', '{hq2}', {website_sql});\n"
            )

        out.write("\n")

        for job_id, company_id, title, location, emp_type, job_level, posting_date, deadline, season, is_open in job_rows:
            out.write(
                "INSERT INTO job_posting "
                "(job_id, company_id, title, location, employment_type, job_level, posting_date, application_deadline, season, is_open) VALUES "
                f"({job_id}, {company_id}, '{title}', '{location}', '{emp_type}', '{job_level}', "
                f"'{posting_date}', '{deadline}', '{season}', {is_open});\n"
            )

if __name__ == "__main__":
    main()
