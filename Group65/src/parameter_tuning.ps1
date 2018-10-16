function InvokeFunction
{

    Param ([int]$populationSize, [int]$fittestSize, [int]$recombinationSize, [int]$mutationSize, [string]$function)

    $maxScore = 0
    $minScore = -1
    $avgScore = -1
    $i = 0
    while ($i -lt 100)
    {
        $command = '& java -DpopulationSize=' + $populationSize + ' -DfittestSize=' + $fittestSize + ' -DrecombinationSize=' + $recombinationSize + ' -DmutationSize=' + $mutationSize + " -jar testrun.jar -submission=player65 -evaluation=" + $function + ' -seed=1 2>&1'
        # Write-Host $command
        $output = Invoke-Expression $command
        # Write-Host $output
        $splittedOutput = $output.split(' ')
        # Write-Host "splitted output length - " $splittedOutput.length
        $currentScore = [System.Decimal]($splittedOutput[$splittedOutput.Length - 3])
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

        $i++
        Start-Sleep -Seconds 1
    }

    return $maxScore, $minScore, $avgScore
}

function TuneFunction
{
    Param ([string]$function)
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
                $currentMaxScore, $currentMinScore, $currentScore = InvokeFunction -populationSize $populationSize -fittestSize $i -recombinationSize $j -mutationSize $k -function $function
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

Write-Host "Starting parameter tuning:"

Write-Host "Compiling..."
$compilationOutput = & javac -Werror -cp .\contest.jar player65.java *.java -Xlint:unchecked 2>&1
if (!([string]::IsNullOrEmpty($compilationOutput)))
{
    Write-Host $compilationOutput
    Write-Host "Error occured while compiling. Aborting script..."
    return
}

jar cmf MainClass.txt submission.jar player65.class *.class
jar uf submission.jar player65.class *.class

# TuneFunction -function SphereEvaluation
# TuneFunction -function BentCigarFunction
# TuneFunction -function SchaffersEvaluation
# TuneFunction -function KatsuuraEvaluation
# $maxScore, $minScore, $avgScore = & InvokeFunction -populationSize 32 -fittestSize 4 -recombinationSize 16 -mutationSize 12 -function SphereEvaluation 2>&1
# Write-Host "Sphere: min - " $minScore "| max - " $maxScore "| avg - $avgScore"
# $maxScore, $minScore, $avgScore = & InvokeFunction -populationSize 34 -fittestSize 6 -recombinationSize 16 -mutationSize 12 -function BentCigarFunction 2>&1
# Write-Host "Bent cigar: min - $minScore | max - $maxScore | avg - $avgScore"
# $maxScore, $minScore, $avgScore = & InvokeFunction -populationSize 45 -fittestSize 0 -recombinationSize 38 -mutationSize 6 -function SchaffersEvaluation 2>&1
# Write-Host "Schaffers: min - $minScore | max - $maxScore | avg - $avgScore"
# $maxScore, $minScore = & InvokeFunction -populationSize 45 -fittestSize 3 -recombinationSize 36 -mutationSize 6 -function KatsuuraEvaluation 2>&1
# Write-Host "Katsuura: min - " $minScore "| max - " $maxScore