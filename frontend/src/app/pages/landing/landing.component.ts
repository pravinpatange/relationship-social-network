import { Component, OnInit, OnDestroy, ElementRef, ViewChild, AfterViewInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-landing",
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: "./landing.component.html",
  styleUrl: "./landing.component.css"
})
export class LandingComponent implements AfterViewInit, OnDestroy {
  @ViewChild("canvas") canvasRef!: ElementRef<HTMLCanvasElement>;
  private animFrame = 0;
  private nodes: any[] = [];
  private mouse = { x: 0, y: 0 };

  features = [
    { icon: "hub", title: "Relationship Groups", desc: "Organize connections into Family, Corporate, Travel and custom groups. Every bond in its own context." },
    { icon: "visibility", title: "Smart Visibility", desc: "Share with Public, All Friends, Selected Groups, or keep it Private. Total control over every post." },
    { icon: "tune", title: "Active Mode", desc: "Switch between Family Mode, Corporate Mode or All Mode. Your feed reshapes around your current world." },
    { icon: "chat_bubble_outline", title: "Context Chat", desc: "Chat within relationship contexts. Family messages stay in Family mode. Work stays at work." },
    { icon: "notifications_none", title: "Mode-Aware Alerts", desc: "Notifications respect your active mode. No cross-context noise, ever." },
    { icon: "auto_awesome", title: "AI Insights (v4)", desc: "Relationship intelligence — smart suggestions, connection scores and natural language search coming in v4." },
  ];

  stats = [
    { value: "4", label: "Visibility Levels" },
    { value: "∞", label: "Custom Groups" },
    { value: "42", label: "REST APIs" },
    { value: "0", label: "Data Sold" },
  ];

  ngAfterViewInit() { this.initCanvas(); window.addEventListener("mousemove", this.onMouse); }
  ngOnDestroy() { cancelAnimationFrame(this.animFrame); window.removeEventListener("mousemove", this.onMouse); }

  onMouse = (e: MouseEvent) => { this.mouse.x = e.clientX; this.mouse.y = e.clientY; };

  initCanvas() {
    const canvas = this.canvasRef.nativeElement;
    const ctx = canvas.getContext("2d")!;
    const resize = () => { canvas.width = window.innerWidth; canvas.height = window.innerHeight; };
    resize();
    window.addEventListener("resize", resize);

    const N = 80;
    this.nodes = Array.from({length: N}, () => ({
      x: Math.random() * canvas.width,
      y: Math.random() * canvas.height,
      vx: (Math.random() - 0.5) * 0.4,
      vy: (Math.random() - 0.5) * 0.4,
      r: Math.random() * 2.5 + 1,
      hue: Math.random() > 0.5 ? 262 : 345,
    }));

    const draw = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      this.nodes.forEach(n => {
        n.x += n.vx; n.y += n.vy;
        if (n.x < 0 || n.x > canvas.width) n.vx *= -1;
        if (n.y < 0 || n.y > canvas.height) n.vy *= -1;
        const dx = this.mouse.x - n.x, dy = this.mouse.y - n.y;
        const dist = Math.sqrt(dx*dx + dy*dy);
        if (dist < 120) { n.x -= dx * 0.01; n.y -= dy * 0.01; }
        ctx.beginPath();
        ctx.arc(n.x, n.y, n.r, 0, Math.PI * 2);
        ctx.fillStyle = `hsla(${n.hue},80%,65%,0.7)`;
        ctx.fill();
      });
      for (let i = 0; i < this.nodes.length; i++) {
        for (let j = i + 1; j < this.nodes.length; j++) {
          const a = this.nodes[i], b = this.nodes[j];
          const d = Math.sqrt((a.x-b.x)**2 + (a.y-b.y)**2);
          if (d < 130) {
            ctx.beginPath();
            ctx.moveTo(a.x, a.y); ctx.lineTo(b.x, b.y);
            const alpha = (1 - d/130) * 0.25;
            ctx.strokeStyle = `rgba(124,58,237,${alpha})`;
            ctx.lineWidth = 0.8;
            ctx.stroke();
          }
        }
      }
      this.animFrame = requestAnimationFrame(draw);
    };
    draw();
  }
}