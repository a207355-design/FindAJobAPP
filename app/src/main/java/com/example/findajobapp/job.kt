package com.example.findajobapp

data class Job(
    val title: String,
    val company: String,
    val location: String,
    val time: String,
    val salary: String
    //如果要变表的话这里也要添加东西
)

val jobList = listOf(
    Job(
        "Compliance & Oversight Lead Manager",
        "Pfizer",
        "Boxhurst, Tadworth (KT20)",
        "6 hours ago",
        "15K–20K"
    ),
    Job(
        "Health Affairs Manager UK&I",
        "Mars",
        "Welham, Castle Cary (BA7)",
        "17 hours ago",
        "12K–18K"
    ),
    Job("Family Liaison Officer", "Witherslack Group", "Guernsey (GY1)", "17 hours ago", "10K–15K"),
    Job("QA Tester", "TechSoft", "London (EC1A)", "2 days ago", "8K–12K"),
    Job("Business Analyst", "Deloitte", "Manchester (M1)", "1 day ago", "12K–16K"),
    Job("Customer Service Representative", "Amazon", "Birmingham (B1)", "3 days ago", "6K–9K"),
    Job("Mobile Vehicle Technician", "Halfords", "Leeds (LS1)", "5 hours ago", "9K–13K"),
    Job("Clinical Psychologist", "NHS", "Oxford (OX1)", "4 days ago", "14K–20K"),
    Job("Waiter / Waitress", "Hilton", "Liverpool (L1)", "6 days ago", "4K–6K"),
    Job("Pharmacist", "Boots", "Cambridge (CB1)", "2 days ago", "13K–18K"),

//    new add job
    Job("Android Developer", "Google", "London (E1)", "3 hours ago", "18K–25K"),
    Job("Frontend Developer", "Meta", "Remote", "1 day ago", "15K–22K"),
    Job("Backend Engineer", "Netflix", "Remote", "2 days ago", "20K–30K"),
    Job("UI/UX Designer", "Adobe", "Manchester (M2)", "5 hours ago", "12K–18K"),
    Job("Data Analyst", "IBM", "Bristol (BS1)", "6 hours ago", "10K–16K"),
    Job("Software Engineer", "Microsoft", "Cambridge (CB2)", "8 hours ago", "18K–28K"),
    Job("Cyber Security Specialist", "Cisco", "London (EC2)", "1 day ago", "20K–32K"),
    Job("DevOps Engineer", "Oracle", "Remote", "3 days ago", "17K–26K"),
    Job("Game Developer", "Ubisoft", "Leamington Spa (CV31)", "2 days ago", "14K–20K"),
    Job("AI Engineer", "OpenAI", "Remote", "4 hours ago", "25K–40K"),
    Job("Cloud Engineer", "AWS", "London (E14)", "7 hours ago", "20K–35K"),
    Job("IT Support Specialist", "HP", "Birmingham (B2)", "1 day ago", "7K–11K"),
    Job("Network Engineer", "Huawei", "Reading (RG1)", "2 days ago", "12K–18K"),
    Job("Product Manager", "TikTok", "London (W1)", "6 hours ago", "18K–27K"),
    Job("Machine Learning Engineer", "Tesla", "Remote", "5 hours ago", "22K–38K"),
    Job("Junior Software Engineer", "Petronas Digital", "Kuala Lumpur (KLCC)", "2 hours ago", "8K–12K"),
    Job("Cloud System Administrator", "Cyberjaya Tech Hub", "Cyberjaya", "5 hours ago", "10K–15K"),
    Job("Frontend Developer", "Grab", "Petaling Jaya (PJ)", "1 day ago", "9K–14K"),
    Job("Data Scientist", "Shopee", "Kuala Lumpur (South Bangsar)", "4 hours ago", "12K–18K"),
    Job("University Research Assistant", "UKM", "Bangi (Selangor)", "1 day ago", "4K–7K"),
    Job("Automation Technician", "Bangi Industrial Area", "Bangi (Selangor)", "3 hours ago", "6K–9K"),
    Job("Digital Marketing Executive", "Selangor Media Group", "Bangi (Selangor)", "6 hours ago", "5K–8K")
)