package com.harish.floatiq.ui.ai.formula

object FormulaDatabase {

    val formulas = listOf(

        FormulaInfo(
            name = "Quadratic Formula",

            category = FormulaCategory.MATHEMATICS,

            formula = "x = (-b ± √(b² - 4ac)) / 2a",

            explanation =
                "Used to solve quadratic equations.",

            variables =
                "a, b, c = coefficients",

            example =
                "x² + 5x + 6 = 0",

            applications =
                "Algebra, engineering, physics",

            keywords =
                listOf(
                    "quadratic",
                    "equation",
                    "roots",
                    "algebra"
                )
        ),

        FormulaInfo(
            name = "Pythagorean Theorem",

            category = FormulaCategory.MATHEMATICS,

            formula = "a² + b² = c²",

            explanation =
                "Relationship between sides of a right triangle.",

            variables =
                "a,b = sides, c = hypotenuse",

            example =
                "3² + 4² = 5²",

            applications =
                "Geometry, construction",

            keywords =
                listOf(
                    "triangle",
                    "geometry",
                    "hypotenuse"
                )
        ),

        FormulaInfo(
            name = "Area of Circle",

            category = FormulaCategory.MATHEMATICS,

            formula = "A = πr²",

            explanation =
                "Calculates area of a circle.",

            variables =
                "r = radius",

            example =
                "r = 5",

            applications =
                "Geometry",

            keywords =
                listOf(
                    "circle",
                    "area",
                    "radius"
                )
        ),

        FormulaInfo(
            name = "Kinetic Energy",

            category = FormulaCategory.PHYSICS,

            formula = "KE = 1/2 mv²",

            explanation =
                "Energy due to motion.",

            variables =
                "m = mass, v = velocity",

            example =
                "Mass = 10kg, Velocity = 5m/s",

            applications =
                "Mechanics",

            keywords =
                listOf(
                    "energy",
                    "motion",
                    "physics"
                )
        ),

        FormulaInfo(
            name = "Ohm's Law",

            category = FormulaCategory.PHYSICS,

            formula = "V = IR",

            explanation =
                "Voltage equals current multiplied by resistance.",

            variables =
                "V = Voltage, I = Current, R = Resistance",

            example =
                "10V = 2A × 5Ω",

            applications =
                "Electronics",

            keywords =
                listOf(
                    "electricity",
                    "voltage",
                    "current"
                )
        ),
        FormulaInfo(
            name = "Distance Formula",

            category =
                FormulaCategory.MATHEMATICS,

            formula =
                "d = √((x₂-x₁)² + (y₂-y₁)²)",

            explanation =
                "Distance between two points.",

            variables =
                "x₁,y₁,x₂,y₂",

            example =
                "(1,2) and (4,6)",

            applications =
                "Coordinate geometry",

            keywords =
                listOf(
                    "distance",
                    "points",
                    "geometry"
                )
        ),
        FormulaInfo(
            name = "Potential Energy",

            category =
                FormulaCategory.PHYSICS,

            formula =
                "PE = mgh",

            explanation =
                "Stored gravitational energy.",

            variables =
                "m,mass g,gravity h,height",

            example =
                "10kg at 5m",

            applications =
                "Mechanics",

            keywords =
                listOf(
                    "energy",
                    "gravity",
                    "height"
                )
        ),
        FormulaInfo(
            name = "Momentum",

            category =
                FormulaCategory.PHYSICS,

            formula =
                "p = mv",

            explanation =
                "Product of mass and velocity.",

            variables =
                "m,v",

            example =
                "5kg × 4m/s",

            applications =
                "Mechanics",

            keywords =
                listOf(
                    "momentum",
                    "mass",
                    "velocity"
                )
        ),
        FormulaInfo(
            name = "Mean",

            category =
                FormulaCategory.STATISTICS,

            formula =
                "Mean = Σx / n",

            explanation =
                "Average of values.",

            variables =
                "Σx,n",

            example =
                "2,4,6",

            applications =
                "Statistics",

            keywords =
                listOf(
                    "average",
                    "mean",
                    "statistics"
                )
        ),
        FormulaInfo(
            name = "Slope Formula",

            category = FormulaCategory.MATHEMATICS,

            formula = "m = (y₂ - y₁)/(x₂ - x₁)",

            explanation = "Calculates slope of a line.",

            variables = "x₁,y₁,x₂,y₂",

            example = "(1,2),(3,6)",

            applications = "Coordinate geometry",

            keywords = listOf(
                "slope",
                "line",
                "gradient"
            )
        ),
        FormulaInfo(
            name = "Area of Triangle",

            category = FormulaCategory.MATHEMATICS,

            formula = "A = 1/2 bh",

            explanation = "Area of a triangle.",

            variables = "b = base, h = height",

            example = "b=10, h=6",

            applications = "Geometry",

            keywords = listOf(
                "triangle",
                "area",
                "geometry"
            )
        ),
        FormulaInfo(
            name = "Circumference of Circle",

            category = FormulaCategory.MATHEMATICS,

            formula = "C = 2πr",

            explanation = "Distance around a circle.",

            variables = "r = radius",

            example = "r = 5",

            applications = "Geometry",

            keywords = listOf(
                "circle",
                "circumference",
                "radius"
            )
        ),
        FormulaInfo(
            name = "Simple Interest",

            category = FormulaCategory.MATHEMATICS,

            formula = "SI = (PRT)/100",

            explanation = "Calculates simple interest.",

            variables = "P,R,T",

            example = "1000,5%,2 years",

            applications = "Finance",

            keywords = listOf(
                "interest",
                "finance",
                "bank"
            )
        ),
        FormulaInfo(
            name = "Compound Interest",

            category = FormulaCategory.MATHEMATICS,

            formula = "A = P(1+r/n)^(nt)",

            explanation = "Compound growth formula.",

            variables = "P,r,n,t",

            example = "1000 at 5%",

            applications = "Finance",

            keywords = listOf(
                "compound",
                "interest",
                "finance"
            )
        ),
        FormulaInfo(
            name = "Newton's Second Law",

            category = FormulaCategory.PHYSICS,

            formula = "F = ma",

            explanation = "Force equals mass times acceleration.",

            variables = "F,m,a",

            example = "10kg × 2m/s²",

            applications = "Mechanics",

            keywords = listOf(
                "force",
                "newton",
                "acceleration"
            )
        ),
        FormulaInfo(
            name = "Work Done",

            category = FormulaCategory.PHYSICS,

            formula = "W = Fd",

            explanation = "Work equals force multiplied by distance.",

            variables = "F,d",

            example = "20N × 5m",

            applications = "Mechanics",

            keywords = listOf(
                "work",
                "force",
                "distance"
            )
        ),
        FormulaInfo(
            name = "Power",

            category = FormulaCategory.PHYSICS,

            formula = "P = W/t",

            explanation = "Rate of doing work.",

            variables = "W,t",

            example = "100J in 10s",

            applications = "Engineering",

            keywords = listOf(
                "power",
                "work",
                "energy"
            )
        ),
        FormulaInfo(
            name = "Wave Speed",

            category = FormulaCategory.PHYSICS,

            formula = "v = fλ",

            explanation = "Speed of a wave.",

            variables = "v,f,λ",

            example = "50Hz × 2m",

            applications = "Waves",

            keywords = listOf(
                "wave",
                "frequency",
                "wavelength"
            )
        ),
        FormulaInfo(
            name = "Density",

            category = FormulaCategory.PHYSICS,

            formula = "ρ = m/V",

            explanation = "Mass per unit volume.",

            variables = "m,V",

            example = "100kg / 2m³",

            applications = "Material science",

            keywords = listOf(
                "density",
                "mass",
                "volume"
            )
        ),
        FormulaInfo(
            name = "Molarity",

            category = FormulaCategory.CHEMISTRY,

            formula = "M = moles / volume",

            explanation = "Concentration of solution.",

            variables = "moles, volume",

            example = "2 mol / 1L",

            applications = "Chemistry",

            keywords = listOf(
                "molarity",
                "solution",
                "chemistry"
            )
        ),
        FormulaInfo(
            name = "Ideal Gas Law",

            category = FormulaCategory.CHEMISTRY,

            formula = "PV = nRT",

            explanation = "Relates pressure, volume and temperature.",

            variables = "P,V,n,R,T",

            example = "Gas calculations",

            applications = "Thermodynamics",

            keywords = listOf(
                "gas",
                "pressure",
                "temperature"
            )
        ),
        FormulaInfo(
            name = "pH Formula",

            category = FormulaCategory.CHEMISTRY,

            formula = "pH = -log[H⁺]",

            explanation = "Measures acidity.",

            variables = "H⁺ concentration",

            example = "0.001M",

            applications = "Chemistry",

            keywords = listOf(
                "ph",
                "acid",
                "base"
            )
        ),
        FormulaInfo(
            name = "Median",

            category = FormulaCategory.STATISTICS,

            formula = "Middle value after sorting",

            explanation = "Central value of dataset.",

            variables = "Data set",

            example = "1,2,3,4,5",

            applications = "Statistics",

            keywords = listOf(
                "median",
                "statistics",
                "data"
            )
        ),
        FormulaInfo(
            name = "Mode",

            category = FormulaCategory.STATISTICS,

            formula = "Most frequent value",

            explanation = "Most repeated observation.",

            variables = "Data set",

            example = "1,2,2,3",

            applications = "Statistics",

            keywords = listOf(
                "mode",
                "frequency",
                "statistics"
            )
        ),
        FormulaInfo(
            name = "Standard Deviation",

            category = FormulaCategory.STATISTICS,

            formula = "σ = √(Σ(x-μ)²/N)",

            explanation = "Measures spread of data.",

            variables = "x,μ,N",

            example = "Dataset analysis",

            applications = "Data science",

            keywords = listOf(
                "standard deviation",
                "statistics",
                "variance"
            )
        ),



    )
}