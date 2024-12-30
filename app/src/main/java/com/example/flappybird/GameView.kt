package com.example.flappybird

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    // Bird properties
    private var birdY = 500f
    private val birdRadius = 40f
    private var birdVelocity = 0f
    private val gravity = 2f

    // Obstacle properties
    private val obstacleWidth = 70f
    private val obstacleGap = 400f
    private var obstacleX = 800f
    private val obstacleSpeed = 10f
    private var upperObstacleHeight = 150f // Height for the upper pole
    private var lowerObstacleHeight = 150f // Height for the lower pole

    // Game properties
    private var isGameOver = false
    private val paint = Paint()
    private val handler = Handler()
    private var score = 0

    // Runnable for game loop
    private val runnable = Runnable {
        if (!isGameOver) {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background color
        canvas.drawColor(Color.CYAN)

        // Draw bird
        paint.color = Color.YELLOW
        canvas.drawCircle(200f, birdY, birdRadius, paint)

        // Update bird position based on velocity and gravity
        if (!isGameOver) {
            birdY += birdVelocity
            birdVelocity += gravity
        }

        // Draw upper obstacle
        paint.color = Color.GREEN
        canvas.drawRect(obstacleX, 0f, obstacleX + obstacleWidth, upperObstacleHeight, paint)

        // Draw lower obstacle
        val lowerObstacleTop = upperObstacleHeight + obstacleGap
        canvas.drawRect(
            obstacleX,
            lowerObstacleTop,
            obstacleX + obstacleWidth,
            height.toFloat(),
            paint
        )

        // Move obstacle
        obstacleX -= obstacleSpeed

        // Reset obstacle if it moves off screen
        if (obstacleX + obstacleWidth < 0) {
            obstacleX = width.toFloat()
            generateRandomObstacleHeights()
            score++
        }

        // Check for collisions
        if (checkCollision() || birdY - birdRadius < 0 || birdY + birdRadius > height) {
            isGameOver = true
            showGameOverDialog() // Show custom game over dialog
        } else {
            // Display score
            paint.color = Color.BLACK
            paint.textSize = 50f
            canvas.drawText("Score: $score", 50f, 100f, paint)
        }

        if (!isGameOver) {
            handler.postDelayed(runnable, 25) // Update game loop every 20ms
        }
    }

    private fun checkCollision(): Boolean {
        val lowerObstacleTop = upperObstacleHeight + obstacleGap
        return obstacleX < 200f + birdRadius &&
                obstacleX + obstacleWidth > 200f - birdRadius &&
                (birdY - birdRadius < upperObstacleHeight || birdY + birdRadius > lowerObstacleTop)
    }

    private fun generateRandomObstacleHeights() {
        // Generate random heights for the upper and lower poles
        val maxUpperHeight = (height / 2) - obstacleGap / 2
        val minUpperHeight = 100
        upperObstacleHeight = Random.nextInt(minUpperHeight, maxUpperHeight.toInt()).toFloat()

        // Lower obstacle height is determined by the remaining space
        val lowerMinHeight = 100
        lowerObstacleHeight = height - (upperObstacleHeight + obstacleGap).coerceAtLeast(lowerMinHeight.toFloat())
    }

    // Method to handle bird's upward motion (jump)
    fun moveUp() {
        if (!isGameOver) {
            birdVelocity = -19f // Set upward velocity for a smoother jump
        }
    }

    // Method to handle bird's downward motion (fall)
    fun moveDown() {
        if (!isGameOver) {
            birdVelocity += 10f // Gradually increase downward velocity
        }
    }

    // Reset game for restarting
    fun resetGame() {
        birdY = 500f
        birdVelocity = 0f
        obstacleX = width.toFloat()
        generateRandomObstacleHeights()
        score = 0
        isGameOver = false
        invalidate()
    }

    // Show custom Game Over dialog
    private fun showGameOverDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Game Over")
        builder.setMessage("Your score is: $score")
        builder.setCancelable(false)

        // Restart button
        builder.setPositiveButton("Restart") { _, _ ->
            resetGame()
        }

        // Exit button
        builder.setNegativeButton("Exit") { _, _ ->
            // Exit the game or close the app
            (context as? MainActivity)?.finish() // assuming you are using MainActivity
        }

        // Show the dialog
        builder.show()
    }
}