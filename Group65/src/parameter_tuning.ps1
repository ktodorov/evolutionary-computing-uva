function CalculateAverage
{
    Param ([double[]]$array)

    $sum = 0
    foreach ($item in $array)
    {
        $sum += $item
    }

    $result = ($sum / $array.Length)
    return $result
}

function CalculateStandardDeviation
{
    Param ([double[]]$array)

    $average = CalculateAverage $array
    $numerator = 0.0
    foreach ($item in $array)
    {
        $numerator += [System.Math]::Pow($item - $average, 2)
    }

    $result = [System.Math]::Sqrt($numerator / ($array.Length - 1))
    return $result
}

function InvokeFunction
{

    Param ([int]$populationSize, [int]$fittestSize, [int]$recombinationSize, [int]$mutationSize, [string]$function, [string]$parentSelectionType, [int]$runs)

    Write-Host "Starting $function with selection: $parentSelectionType"

    $maxScore = 0
    $minScore = -1
    $avgScore = -1
    $avgCycleNumber = -1
    $i = 0
    $cyclesCount = 100
    $scores = @()
    $cycles = @()
    $maxFitnessPerCycle = @()

    for ($j = 0; $j -lt $cyclesCount; $j++)
    {
        $maxFitnessPerCycle += 0.0
    }

    while ($i -lt $runs)
    {      
        $command = '& java -DpopulationSize=' + $populationSize + ' -DfittestSize=' + $fittestSize + ' -DrecombinationSize=' + 
                    $recombinationSize + ' -DmutationSize=' + $mutationSize + " -DparentSelectionType=" + $parentSelectionType +
                    " -jar testrun.jar -submission=player65 -evaluation=" + $function + ' -seed=1 2>&1'
        # Write-Host $command
        $output = Invoke-Expression $command
        # Write-Host $output
        $splittedOutput = $output.split(' ')
        
        $maxFitnessPerCycle[0] = ($maxFitnessPerCycle[0], [System.Decimal]$splittedOutput[0] | Measure-Object -Max).Maximum

        for ($j = 1; $j -lt $cyclesCount; $j++)
        {
            $newDecimal = [System.Decimal]$splittedOutput[$j]
            $maxFitnessPerCycle[$j] = ($maxFitnessPerCycle[$j], $newDecimal, $maxFitnessPerCycle[$j - 1] | Measure-Object -Max).Maximum
        }

        # Write-Host "splitted output length - " $splittedOutput.length
        $currentScore = [System.Decimal]($splittedOutput[$splittedOutput.Length - 3])
        $scores += $currentScore

        $cycleNumber = [System.Decimal]($splittedOutput[$splittedOutput.Length - 5])
        $cycles += $cycleNumber

        # Write-Host "Current score - " $currentScore
        if ($maxScore -lt $currentScore)
        {
            $maxScore = $currentScore
        }

        if ($minScore -gt $currentScore -or $minScore -lt 0)
        {
            $minScore = $currentScore
        }

        if ($avgScore -eq -1) 
        {
            $avgScore = $currentScore
        }
        else 
        {
            $avgScore = ($currentScore + $avgScore)/2
        }

        if ($avgCycleNumber -eq -1) 
        {
            $avgCycleNumber = $cycleNumber
        }
        else 
        {
            $avgCycleNumber = ($cycleNumber + $avgCycleNumber)/2
        }

        $i++
    }

    $scoresStandardDeviation = CalculateStandardDeviation $scores
    $cyclesStandardDeviation = CalculateStandardDeviation $cycles

    return $maxScore, $minScore, $avgScore, $avgCycleNumber, $scoresStandardDeviation, $cyclesStandardDeviation, $maxFitnessPerCycle
}

function TuneFunction
{
    Param ([string]$function, [string]$parentSelectionType)
    $maxScore = 0
    $populationSize = 45
    $bestPopulationSize = 0
    $bestFittestSize = 0
    $bestRecombinationSize = 0
    $bestMutationSize = 0
    
    For ($i = 0; $i -lt $populationSize; $i+=2)
    {
        Write-Host "function - $function $i/$populationSize"
        For ($j = 2; $j -le $populationSize - $i; $j+=2)
        {
            For ($k = 0; $k -le $populationSize - $i - $j -and $k -le 15; $k+=2)
            {
                $currentMaxScore, $currentMinScore, $currentScore = InvokeFunction -populationSize $populationSize -fittestSize $i -recombinationSize $j -mutationSize $k -function $function -parentSelectionType $parentSelectionType
                # Write-Host "current score - " $currentScore
                if ($maxScore -lt $currentScore)
                {
                    $maxScore = $currentScore
                    $bestPopulationSize = $populationSize
                    $bestFittestSize = $i
                    $bestRecombinationSize = $j
                    $bestMutationSize = $k
                }
            }
        }
    }

    Write-Host $function " best result:"
    Write-Host " - Score - " $maxScore
    Write-Host " - Population size - " $bestPopulationSize
    Write-Host " - Fittest size - " $bestFittestSize
    Write-Host " - Recombination size - " $bestRecombinationSize
    Write-Host " - Mutation size - " $bestMutationSize
    return $maxScore, $bestPopulationSize, $bestFittestSize, $bestRecombinationSize, $bestMutationSize
}

function WriteFunctionResult
{
    Param (
    [string]$parentSelectionType, 
    [int]$populationSize, 
    [int]$fittestSize, 
    [int]$recombinationSize, 
    [int]$mutationSize, 
    [string]$function, 
    [double]$minScore, 
    [double]$maxScore, 
    [double]$avgScore, 
    [double]$avgCycleNumber, 
    [double]$scoresStandardDeviation, 
    [double]$cyclesStandardDeviation, 
    [double[]]$maxFitnessPerCycle)

    Write-Host 
"Parent selection type: $parentSelectionType`npopulation size: $populationSize`nfittest size: $fittestSize
recombination size: $recombinationSize`nmutation size: $mutationSize`nfunction: $function`n - min: $minScore
 - max: $maxScore`n - avg: $avgScore`n - avg cycle found: $avgCycleNumber`n - scores standard deviation: $scoresStandardDeviation
 - cycles standard deviation: $cyclesStandardDeviation`n - max fitnesses per cycle: $maxFitnessPerCycle"
    
}

function SaveToExcel
{
    Param (
    [string]$parentSelectionType, 
    [int]$populationSize, 
    [int]$fittestSize, 
    [int]$recombinationSize, 
    [int]$mutationSize, 
    [string]$function, 
    [double]$minScore, 
    [double]$maxScore, 
    [double]$avgScore, 
    [double]$avgCycleNumber, 
    [double]$scoresStandardDeviation, 
    [double]$cyclesStandardDeviation, 
    [double[]]$maxFitnessPerCycle)

    $excel = New-Object -ComObject Excel.Application 
    $excel.Visible = $True
    $workbook = $excel.Workbooks.Add()
    $firstWorksheet = $workbook.Worksheets.Item(1) 

    $firstWorksheet.Cells.Item(1,1) = "Parent selection type"
    $firstWorksheet.Cells.Item(1,2) = "Population size"
    $firstWorksheet.Cells.Item(1,3) = "Fittest size"
    $firstWorksheet.Cells.Item(1,4) = "Recombination size"
    $firstWorksheet.Cells.Item(1,5) = "Mutation size"
    $firstWorksheet.Cells.Item(1,6) = "Function"
    $firstWorksheet.Cells.Item(1,7) = "Minimum score"
    $firstWorksheet.Cells.Item(1,8) = "Maximum score"
    $firstWorksheet.Cells.Item(1,9) = "Average score"
    $firstWorksheet.Cells.Item(1,10) = "Average cycle number"
    $firstWorksheet.Cells.Item(1,11) = "Standard deviation of scores"
    $firstWorksheet.Cells.Item(1,12) = "Standard deviation of cycles"

    $firstWorksheet.Cells.Item(2, 1) = $parentSelectionType
    $firstWorksheet.Cells.Item(2, 2) = $populationSize
    $firstWorksheet.Cells.Item(2, 3) = $fittestSize
    $firstWorksheet.Cells.Item(2, 4) = $recombinationSize
    $firstWorksheet.Cells.Item(2, 5) = $mutationSize
    $firstWorksheet.Cells.Item(2, 6) = $function
    $firstWorksheet.Cells.Item(2, 7) = $minScore
    $firstWorksheet.Cells.Item(2, 8) = $maxScore
    $firstWorksheet.Cells.Item(2, 9) = $avgScore
    $firstWorksheet.Cells.Item(2, 10) = $avgCycleNumber
    $firstWorksheet.Cells.Item(2, 11) = $scoresStandardDeviation
    $firstWorksheet.Cells.Item(2, 12) = $cyclesStandardDeviation

    $firstWorksheet.Cells.Item(4,1) = "Max fitnesses per cycle"

    $col = 1
    foreach ($item in $maxFitnessPerCycle)
    {
        $firstWorksheet.Cells.Item(5,$col) = $item
        $col++
    }

    $firstWorksheet.activate()
    $maxFitnessesDataSheet = $firstWorksheet.Range("A5").CurrentRegion

    $chart = $firstWorksheet.Shapes.AddChart().Chart
    $chart.ChartType = [Microsoft.Office.Interop.Excel.XLChartType]::xlLine
    $chart.SetSourceData($maxFitnessesDataSheet)

    $usedRange = $firstWorksheet.UsedRange
    $usedRange.EntireColumn.AutoFit() | Out-Null

    $xlFixedFormat = [Microsoft.Office.Interop.Excel.XlFileFormat]::xlWorkbookDefault
    $outputPath = "C:\EC_Stats\$function" + "_$parentSelectionType.xlsx"
    $workbook.SaveAs($outputPath, $xlFixedFormat) 
    $workbook.Close()
    $excel.DisplayAlerts = $False
    $excel.Quit()
}

Write-Host "Starting parameter tuning:"

Write-Host "Compiling..."
$compilationOutput = & javac -Werror -cp .\contest.jar player65.java *.java -Xlint:unchecked 2>&1
if (!([string]::IsNullOrEmpty($compilationOutput)))
{
    Write-Host $compilationOutput
    Write-Host "Error occured while compiling. Aborting script..."
    return
}

function ExecuteFunctionAndSaveReport
{
    Param([int]$populationSize, [int]$fittestSize, [int]$recombinationSize, [int]$mutationSize, [string]$function, [string]$parentSelectionType, [int]$runs)

    $maxScore, $minScore, $avgScore, $avgCycleNumber, $scoresStandardDeviation, $cyclesStandardDeviation, $maxFitnessPerCycle = & InvokeFunction $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs 2>&1
    SaveToExcel $parentSelectionType $populationSize $fittestSize $recombinationSize $mutationSize $function $minScore $maxScore $avgScore $avgCycleNumber $scoresStandardDeviation $cyclesStandardDeviation $maxFitnessPerCycle
}

jar cmf MainClass.txt submission.jar player65.class *.class
jar uf testrun.jar player65.class *.class

# TuneFunction -function SphereEvaluation
# TuneFunction -function BentCigarFunction
# TuneFunction -function SchaffersEvaluation
# TuneFunction -function KatsuuraEvaluation
$parentSelectionType = "TOURNAMENT"
$populationSize = 100
$fittestSize = 20
$recombinationSize = 40
$mutationSize = 40
$runs = 100

# $function = "BentCigarFunction"
# ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs


# $populationSize = 32
# $fittestSize = 4
# $recombinationSize = 16
# $mutationSize = 12
$function = "SphereEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

# # $populationSize = 34
# # $fittestSize = 6
# # $recombinationSize = 16
# # $mutationSize = 12
$function = "BentCigarFunction"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

# # $populationSize = 45
# # $fittestSize = 0
# # $recombinationSize = 38
# # $mutationSize = 6
$function = "SchaffersEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

# $populationSize = 45
# $fittestSize = 3
# $recombinationSize = 36
# $mutationSize = 6
$function = "KatsuuraEvaluation"
$runs = 10
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs
$runs = 100


# # Random selection
$parentSelectionType = "RANDOM"
$function = "SphereEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

$function = "BentCigarFunction"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

$function = "SchaffersEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

$runs = 10
$function = "KatsuuraEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs
$runs = 100



# # Roulette wheel selection
$parentSelectionType = "ROULETTE_WHEEL"
$function = "SphereEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

$function = "BentCigarFunction"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

$function = "SchaffersEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs

$runs = 10
$function = "KatsuuraEvaluation"
ExecuteFunctionAndSaveReport $populationSize $fittestSize $recombinationSize $mutationSize $function $parentSelectionType $runs
$runs = 100