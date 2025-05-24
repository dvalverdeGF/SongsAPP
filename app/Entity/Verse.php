<?php

namespace App\Entity;

use App\Repository\VerseRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: VerseRepository::class)]
class Verse
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $text = null;

    #[ORM\Column]
    private ?int $position = null;

    #[ORM\ManyToOne(targetEntity: self::class, inversedBy: 'verses')]
    #[ORM\JoinColumn(nullable: false)]
    private ?self $lyric = null;

    /**
     * @var Collection<int, self>
     */
    #[ORM\OneToMany(targetEntity: self::class, mappedBy: 'lyric', orphanRemoval: true)]
    private Collection $verses;

    /**
     * @var Collection<int, LyricChord>
     */
    #[ORM\OneToMany(targetEntity: LyricChord::class, mappedBy: 'verse', orphanRemoval: true)]
    private Collection $chords;

    /**
     * @var Collection<int, Annotation>
     */
    #[ORM\OneToMany(targetEntity: Annotation::class, mappedBy: 'verse', orphanRemoval: true)]
    private Collection $annotations;

    public function __construct()
    {
        $this->verses = new ArrayCollection();
        $this->chords = new ArrayCollection();
        $this->annotations = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getText(): ?string
    {
        return $this->text;
    }

    public function setText(string $text): static
    {
        $this->text = $text;

        return $this;
    }

    public function getPosition(): ?int
    {
        return $this->position;
    }

    public function setPosition(int $position): static
    {
        $this->position = $position;

        return $this;
    }

    public function getLyric(): ?self
    {
        return $this->lyric;
    }

    public function setLyric(?self $lyric): static
    {
        $this->lyric = $lyric;

        return $this;
    }

    /**
     * @return Collection<int, self>
     */
    public function getVerses(): Collection
    {
        return $this->verses;
    }

    public function addVerse(self $verse): static
    {
        if (!$this->verses->contains($verse)) {
            $this->verses->add($verse);
            $verse->setLyric($this);
        }

        return $this;
    }

    public function removeVerse(self $verse): static
    {
        if ($this->verses->removeElement($verse)) {
            // set the owning side to null (unless already changed)
            if ($verse->getLyric() === $this) {
                $verse->setLyric(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, LyricChord>
     */
    public function getChords(): Collection
    {
        return $this->chords;
    }

    public function addChord(LyricChord $chord): static
    {
        if (!$this->chords->contains($chord)) {
            $this->chords->add($chord);
            $chord->setVerse($this);
        }

        return $this;
    }

    public function removeChord(LyricChord $chord): static
    {
        if ($this->chords->removeElement($chord)) {
            // set the owning side to null (unless already changed)
            if ($chord->getVerse() === $this) {
                $chord->setVerse(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Annotation>
     */
    public function getAnnotations(): Collection
    {
        return $this->annotations;
    }

    public function addAnnotation(Annotation $annotation): static
    {
        if (!$this->annotations->contains($annotation)) {
            $this->annotations->add($annotation);
            $annotation->setVerse($this);
        }

        return $this;
    }

    public function removeAnnotation(Annotation $annotation): static
    {
        if ($this->annotations->removeElement($annotation)) {
            // set the owning side to null (unless already changed)
            if ($annotation->getVerse() === $this) {
                $annotation->setVerse(null);
            }
        }

        return $this;
    }
}
